package com.alibaba.api.keyedprocessfunction;

import com.alibaba.base.bean.WaterSensor;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


import java.util.concurrent.TimeUnit;

/**
 * @author :YuFada
 * @date： 2021/3/8 0008 上午 10:23
 * Description：
 */
public class KeyedProcessFunction {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //配置重启策略
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
                4, // number of restart attempts
              Time.of(10, TimeUnit.SECONDS) // delay
        ));
        env.setParallelism(4);
        DataStreamSource<String> dataDs = env.readTextFile("input/sensor-data.log");
        SingleOutputStreamOperator<WaterSensor> mapDs = dataDs.map(new MapFunction<String, WaterSensor>() {
            @Override
            public WaterSensor map(String value) throws Exception {
                String[] data = value.split(",");
                return new WaterSensor(data[0],
                        Long.valueOf(data[1]),
                        Integer.valueOf(data[2]));
            }
        });

        SingleOutputStreamOperator<WaterSensor> filterDs = mapDs.filter(vc -> vc.getVc() > 60);


        filterDs.print("filterDs");
        try {
            env.execute("filterDs");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
