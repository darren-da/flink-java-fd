package com.alibaba.job.userbehavior;

import com.alibaba.job.bean.UserBehavior;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author fada.yu
 * @date 2020/10/10 14:07
 * @Description：
 */
public class UserBehaviorPv {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
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

        SingleOutputStreamOperator<Tuple2<String, Integer>> pvAndOneDS = filterDS
                .map(data -> Tuple2.of("pv", 1))
                .returns(new TypeHint<Tuple2<String, Integer>>() {

                });

        KeyedStream<Tuple2<String, Integer>, String> pvAndOneKS = pvAndOneDS.keyBy(behavior -> behavior.f0);
        SingleOutputStreamOperator<Tuple2<String, Integer>> pvDS = pvAndOneKS.sum(1);

        pvDS.print("userBehavior pv");
        env.execute("pv");

    }


}
