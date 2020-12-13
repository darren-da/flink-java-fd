package com.alibaba.api.keyedprocessfunction.suanzi;

import com.alibaba.base.bean.WaterSensor;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author :YuFada
 * @date： 2020/12/13 0013 下午 21:44
 * Description：
 */
public class TestFilter {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> inputDs = env.readTextFile("input/sensor-data.log");

        SingleOutputStreamOperator<WaterSensor> sensorDs = inputDs.map((MapFunction<String, WaterSensor>) value -> {
            String[] datas = value.split(",");
            return new WaterSensor(datas[0], Long.valueOf(datas[1]), Integer.valueOf(datas[2]));
        });
        SingleOutputStreamOperator<WaterSensor> fiterDS1 = sensorDs.filter(new FilterFunction<WaterSensor>() {
            @Override
            public boolean filter(WaterSensor waterSensor) throws Exception {
                return waterSensor.getVc() == 60;
            }
        });

        SingleOutputStreamOperator<WaterSensor> filterDS2 = sensorDs.filter(new FilterFunction<WaterSensor>() {
            @Override
            public boolean filter(WaterSensor waterSensor) throws Exception {
                return waterSensor.getVc() == 80;
            }
        });

        DataStream<WaterSensor> filterUnion = fiterDS1.union(filterDS2);
        filterUnion.print("union");
//        ConnectedStreams<WaterSensor, WaterSensor> connectDs = fiterDS1.connect(filterDS2);

        try {
            env.execute("union");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
