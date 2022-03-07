package com.alibaba.job.demo;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author fada.yu
 * @version 1.0
 * @date 2022/3/7 12:10
 * @Desc:
 */
public class PaySuccessRateJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource inputStream = env.addSource(new MySourceSuccesRate());
        env.setParallelism(1);

        SingleOutputStreamOperator filterSuccess = inputStream.filter(new FilterFunction<PaySuccessRateBean>() {
            @Override
            public boolean filter(PaySuccessRateBean value) throws Exception {
                return value.getPayEnd() != 0;
            }
        });

        SingleOutputStreamOperator filterFailed = inputStream.filter(new FilterFunction<PaySuccessRateBean>() {
            @Override
            public boolean filter(PaySuccessRateBean value) throws Exception {
                return value.getPayEnd() == 0;
            }
        });

        SingleOutputStreamOperator successTuple = filterSuccess.map((MapFunction<PaySuccessRateBean, Tuple2<String, Integer>>) r ->
                new Tuple2<String, Integer>("success", 1))
                .returns(new TypeHint<Tuple2<String, Integer>>() {
                });

        SingleOutputStreamOperator failedTuple = filterFailed.map((MapFunction<PaySuccessRateBean, Tuple2<String, Integer>>) r ->
                new Tuple2<String, Integer>("failed", 1))
                .returns(new TypeHint<Tuple2<String, Integer>>() {
                });

        KeyedStream<Tuple2<String, Integer>, String> sensorKS = successTuple.keyBy(r -> r.f0);


        successTuple.print();

        env.execute("PaySuccessRateTest");
    }
}
