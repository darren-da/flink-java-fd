package com.alibaba.job.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author fada.yu
 * @version 1.0
 * @date 2022/3/7 17:11
 * @Desc:
 */
public class GenData {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<PaySuccessRateBean> inputDS = env.addSource(new MySourceSuccesRate());

        SingleOutputStreamOperator<Object> mapDS = inputDS.map(new MapFunction<PaySuccessRateBean, Object>() {
            @Override
            public Object map(PaySuccessRateBean paySuccessRateBean) throws Exception {
                return JSON.toJSONString(paySuccessRateBean);
            }
        });

        mapDS.print();
        env.execute("genData");
    }
}
