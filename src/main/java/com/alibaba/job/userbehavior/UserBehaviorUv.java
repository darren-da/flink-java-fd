package com.alibaba.job.userbehavior;

import com.alibaba.job.bean.UserBehavior;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.util.HashSet;
import java.util.Set;

/**
 * @author fada.yu
 * @date 2020/10/10 15:27
 * @Description：
 */
public class UserBehaviorUv {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<UserBehavior> userBehaviorDS = env.readTextFile("input/UserBehavior.csv")
                .map(line -> {
                    String[] datas = line.split(",");
                    return new UserBehavior(
                            Long.valueOf(datas[0]),
                            Long.valueOf(datas[1]),
                            Integer.valueOf(datas[2]),
                            datas[3],
                            Long.valueOf(datas[4])
                    );

                });

        SingleOutputStreamOperator<UserBehavior> filterDS = userBehaviorDS.filter(new FilterFunction<UserBehavior>() {
            @Override
            public boolean filter(UserBehavior value) throws Exception {

                return "pv".equals(value.getBehavior());
            }
        });

        //转换成二元组 （uv，userId）
        SingleOutputStreamOperator<Tuple2<String, Long>> uvAndUserIdDS = filterDS.map(new MapFunction<UserBehavior, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> map(UserBehavior value) throws Exception {
                return Tuple2.of("uv", value.getUserId());
            }
        });

        //按照 uv 分组
        KeyedStream<Tuple2<String, Long>, String> uvAndUserIdKS = uvAndUserIdDS.keyBy(tuple2 -> tuple2.f0);
//        5)	对每一条读取的数据进行去重，这里可以采用Java集合Set完成

        SingleOutputStreamOperator<Integer> uvDS = uvAndUserIdKS.process(
                new ProcessFunction<Tuple2<String, Long>, Integer>() {
                    private Set<Long> uvCount = new HashSet<>();

                    @Override
                    public void processElement(Tuple2<String, Long> value, Context context, Collector<Integer> out) throws Exception {
                        uvCount.add(value.f1);
                        out.collect(uvCount.size());


                    }
                }
        );
        uvDS.print();
        env .execute("uv ");

    }
};
