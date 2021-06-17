package com.alibaba.gmall.realtime.utils;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

/**
 * @author :YuFada
 * @date： 2021/6/16 0016 下午 19:00
 * Description：
 */
public class MyKafkaUtils {
    private static String kafkaServer = "hadoop202:9092,hadoop203:9092,hadoop204:9092";

    //封装Kafka消费者
    public static FlinkKafkaConsumer<String> getKafkaSource(String topic, String groupId){
        Properties prop = new Properties();
        prop.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        prop.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaServer);
        return new FlinkKafkaConsumer<String>(topic,new SimpleStringSchema(),prop);
    }
}
