package com.alibaba.base;

import com.alibaba.base.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

/**
 * @author fada.yu
 * @date 2020/10/9 15:41
 * @Description：
 */
public class Flink02_Source_Collection {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<WaterSensor> colleciontDs = env.fromCollection(
                Arrays.asList(
                        new WaterSensor("ws_001", 1577844001L, 45),
                        new WaterSensor("ws_002", 1577844015L, 43),
                        new WaterSensor("ws_003", 1577844020L, 42)
                )
        );
        colleciontDs.print();
        env.execute();
    }
}
