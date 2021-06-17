package com.alibaba.gmall.realtime.app;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.gmall.realtime.utils.MyKafkaUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

/**
 * @author :YuFada
 * @date： 2021/6/16 0016 下午 19:18
 * Description：
 */
public class BaseLogApp {
    //定义用户行为主题信息
    private static final String TOPIC_START ="dwd_start_log";
    private static final String TOPIC_PAGE ="dwd_page_log";
    private static final String TOPIC_DISPLAY ="dwd_display_log";

    public static void main(String[] args) throws Exception {
        //todo for  基本环境准备

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);//最好和kafka分区保持一致

        //设置CK相关的参数
        //设置精准一次性保证（默认）  每5000ms开始一次checkpoint
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        //Checkpoint必须在一分钟内完成，否则就会被抛弃
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        env.setStateBackend(new FsStateBackend("hdfs://hadoop202:8020/gmall/flink/checkpoint"));
        System.setProperty("HADOOP_USER_NAME","fada.yu");


        //指定消费者配置信息
        String groupId = "ods_dwd_base_log_app";
        String topic = "ods_base_log";

        // TODO: 2021/6/16 0016 从kafka中读取数据
        FlinkKafkaConsumer<String> kafkaSource = MyKafkaUtils.getKafkaSource(topic, groupId);
        DataStreamSource<String> kafkaDs = env.addSource(kafkaSource);

        //数据转为json对象
        SingleOutputStreamOperator<JSONObject> jsonObjectDs
                = kafkaDs.map(new MapFunction<String, JSONObject>() {
            @Override
            public JSONObject map(String value) throws Exception {
                return JSONObject.parseObject(value);
            }
        });

        // TODO: 2021/6/16 0016  数据测试
        jsonObjectDs.print();
        env.execute("first test");

        // TODO: 2021/6/16 0016 识别新老客户需求
        KeyedStream<JSONObject, Object> midKeyedDs = jsonObjectDs.keyBy(data ->
           data.getJSONObject("common").getString("mid")
        );


    }
}
