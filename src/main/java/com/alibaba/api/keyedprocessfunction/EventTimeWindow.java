package com.alibaba.api.keyedprocessfunction;

import com.alibaba.base.bean.WaterSensor;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;


import java.util.concurrent.TimeUnit;


/**
 * @author :YuFada
 * @date： 2021/3/6 0006 上午 8:17
 * Description：
 * EventTime在window中的使用
 */
public class EventTimeWindow {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //配置重启策略
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
                4, // number of restart attempts
                org.apache.flink.api.common.time.Time.of(10, TimeUnit.SECONDS) // delay
        ));


        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStreamSource<String> dataStreamSource = env.readTextFile("input/sensor-data.log");
        SingleOutputStreamOperator<WaterSensor> inputStream = dataStreamSource.map(new MapFunction<String, WaterSensor>() {
            @Override
            public WaterSensor map(String value) throws Exception {
                String[] data = value.split(",");
                return new WaterSensor(data[0],
                        Long.valueOf(data[1]),
                        Integer.valueOf(data[2]));
            }
        });
        SingleOutputStreamOperator<WaterSensor> filterDS = inputStream.filter(new FilterFunction<WaterSensor>() {
            @Override
            public boolean filter(WaterSensor waterSensor) throws Exception {
                return waterSensor.getVc() > 50;
            }
        });

        SingleOutputStreamOperator<WaterSensor> waterDs = filterDS.assignTimestampsAndWatermarks(
                new BoundedOutOfOrdernessTimestampExtractor<WaterSensor>(Time.seconds(1)) {
                    @Override
                    public long extractTimestamp(WaterSensor waterSensor) {
                        return waterSensor.getTs()*1000L;
                    }
                }
        );
        SingleOutputStreamOperator<Tuple2<String, Integer>> tupleDs = waterDs.map((MapFunction<WaterSensor, Tuple2<String, Integer>>) r -> new Tuple2<String, Integer>(r.getId(), r.getVc()))
                .returns(new TypeHint<Tuple2<String, Integer>>() {
                    @Override
                    public TypeInformation<Tuple2<String, Integer>> getTypeInfo() {
                        return super.getTypeInfo();
                    }
                });
        SingleOutputStreamOperator<Tuple2<String, Integer>> reduceResult
                = tupleDs.keyBy(r -> r.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(1)))
                .reduce((v1, v2) -> new Tuple2<String, Integer>(v1.f0, v1.f1 + v2.f1));

        reduceResult.print("resultReduce");

        try {
            env.execute("sensordata");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
