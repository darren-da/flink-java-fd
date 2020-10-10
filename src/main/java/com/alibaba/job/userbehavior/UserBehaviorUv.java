package com.alibaba.job.userbehavior;

import com.alibaba.job.userbehavior.bean.UserBehavior;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author fada.yu
 * @date 2020/10/10 15:27
 * @Description：
 */
public class UserBehaviorUv {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<UserBehavior> userBehaviorDS = env.readTextFile("D:\\Personal\\workspace\\dp-flink-workspace-fada\\flink-java\\src\\main\\resources\\input\\UserBehavior.csv")
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

    }

}

