package com.alibaba.job.txcheck;

import com.alibaba.job.bean.OrderEvent;
import com.alibaba.job.bean.TxEvent;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;

import java.util.HashMap;

/**
 * @author fada.yu
 * @date 2020/10/21 10:05
 * @Description： Real-time reconciliation
 */
public class TxCheck {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<String> inputDs = env.readTextFile("input/OrderLog.csv");
        SingleOutputStreamOperator<OrderEvent> orderDs = inputDs.map(new MapFunction<String, OrderEvent>() {
            @Override
            public OrderEvent map(String value) throws Exception {
                String[] data = value.split(",");

                return new OrderEvent(Long.valueOf(data[0]),
                        data[1],
                        data[2],
                        Long.valueOf(data[3]));
            }
        });

        SingleOutputStreamOperator<TxEvent> txDS = env.readTextFile("input/ReceiptLog.csv")
                .map(new MapFunction<String, TxEvent>() {
                    @Override
                    public TxEvent map(String value) throws Exception {
                        String[] data = value.split(",");

                        return new TxEvent(data[0],
                                data[1],
                                Long.valueOf(data[2]));
                    }
                });


        SingleOutputStreamOperator<String> processDS = orderDs.connect(txDS)
                .keyBy(order -> order.getTxId(), tx -> tx.getTxId())
                .process(new CoProcessFunction<OrderEvent, TxEvent, String>() {
                    //两条流到到达时间不一致，所以要匹配对账信息
                    HashMap<String, OrderEvent> orderMap = new HashMap<>();
                    HashMap<String, TxEvent> txMap = new HashMap<>();

                    @Override
                    public void processElement1(OrderEvent value, Context ctx, Collector<String> out) throws Exception {
                        TxEvent txEvent = txMap.get(value.getTxId());
                        if (txEvent == null) {
                            orderMap.put(value.getTxId(), value);
                        } else {
                            out.collect("订单[" + value.getOrderId() + "]----->对账成功");
                            orderMap.remove(value.getTxId());
                        }

                    }

                    @Override
                    public void processElement2(TxEvent value, Context ctx, Collector<String> out) throws Exception {
                        OrderEvent orderEvent = orderMap.get(value.getTxId());
                        if (orderEvent == null) {
                            txMap.put(value.getTxId(), value);
                        } else {
                            out.collect("订单[" + orderEvent.getOrderId() + "]---->对账成功");
                        }

                    }


                });

        processDS.print("txCheck");
        env.execute("tx Check");
    }
}
