package com.alibaba.base.sink;

import com.alibaba.base.MySource;
import com.alibaba.base.bean.WaterSensor;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

/**
 * @author fada.yu
 * @date 2020/10/10 13:05
 * @Description：
 */
public class KafkaSink {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<WaterSensor> sensorDS = env.addSource(new MySource());

        sensorDS.addSink(new FlinkKafkaProducer<WaterSensor>(
                "hadoop102:9092"
                , "sensor"
                , (KeyedSerializationSchema<WaterSensor>) new SimpleStringSchema()
        ));
    }
}
