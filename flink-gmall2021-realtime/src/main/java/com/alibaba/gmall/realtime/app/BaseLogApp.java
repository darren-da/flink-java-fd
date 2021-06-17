package com.alibaba.gmall.realtime.app;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.gmall.realtime.utils.MyKafkaUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.OutputTag;

import java.text.SimpleDateFormat;
import java.util.Date;

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

        // TODO 1: 2021/6/16 0016 识别新老客户需求
        KeyedStream<JSONObject, String> midKeyedDs = jsonObjectDs.keyBy(data ->
           data.getJSONObject("common").getString("mid")
        );

        // TODO: 2021/6/17 0017  检测采集到的是新老客户
        SingleOutputStreamOperator<JSONObject> midWithNewFlagDS = midKeyedDs.map(new RichMapFunction<JSONObject, JSONObject>() {
            //声明第一次访问日期的状态
            private ValueState<String> firstVisitDataState;
            //声明日期数据格式化对象
            private SimpleDateFormat simpleDateFormat;

            @Override
            public void open(Configuration parameters) throws Exception {
                //初始化数据
                firstVisitDataState = getRuntimeContext().getState(
                        new ValueStateDescriptor<String>("newMidDateState", String.class)
                );
                simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            }

            @Override
            public JSONObject map(JSONObject jsonObj) throws Exception {
                //获取访问标记   0表示老访客  1表示新访客
                String isNew = jsonObj.getJSONObject("common").getString("is_new");
                //获取数据中的时间戳
                Long ts = jsonObj.getLong("ts");

                //判断标记如果为"1",则继续校验数据
                if ("1".equals(isNew)) {
                    //获取新访客状态
                    String newMidDate = firstVisitDataState.value();
                    //获取当前访问的日期
                    String tsDate = simpleDateFormat.format(new Date(ts));
                    //如果新访客状态不为空,说明该设备已访问过 则将访问标记置为"0"
                    if (newMidDate != null && newMidDate.length() != 0) {
                        if (!newMidDate.equals(tsDate)) {
                            isNew = "0";
                            jsonObj.getJSONObject("common").put("is_new", isNew);
                        } else {
                            //如果复检后，该设备的确没有访问过，那么更新状态为当前日期
                            firstVisitDataState.update(tsDate);
                        }
                    }

                }
                return jsonObj;
            }


        });


        // TODO 2: 2021/6/17 0017  利用侧输出流进行数据的拆分
        //定义启动和曝光数据的侧输出流标签
        OutputTag<String> startTag = new OutputTag<>("start");


    }
}
