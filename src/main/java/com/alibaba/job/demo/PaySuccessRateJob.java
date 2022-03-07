package com.alibaba.job.demo;

import org.apache.commons.math3.fitting.leastsquares.EvaluationRmsChecker;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;

import java.util.HashMap;

/**
 * @author fada.yu
 * @version 1.0
 * @date 2022/3/7 12:10
 * @Desc:
 */
public class PaySuccessRateJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource inputStream = env.addSource(new MySourceSuccesRate());
        env.setParallelism(1);
                /*
        //CK相关设置
        env.enableCheckpointing(5000, CheckpointingMode.AT_LEAST_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        StateBackend fsStateBackend = new FsStateBackend(
                "hdfs://hadoop202:8020/gmall/flink/checkpoint/ProvinceStatsSqlApp");
        env.setStateBackend(fsStateBackend);
        System.setProperty("HADOOP_USER_NAME","atguigu");
        */

        SingleOutputStreamOperator filterSuccess = inputStream.filter(new FilterFunction<PaySuccessRateBean>() {
            @Override
            public boolean filter(PaySuccessRateBean value) throws Exception {
                return value.getPayEnd() != 0;
            }
        });

        SingleOutputStreamOperator filterFailed = inputStream.filter(new FilterFunction<PaySuccessRateBean>() {
            @Override
            public boolean filter(PaySuccessRateBean value) throws Exception {
                return value.getPayEnd() == 0;
            }
        });


        SingleOutputStreamOperator<Tuple2<String, Integer>> successTuple = filterSuccess.map((MapFunction<PaySuccessRateBean, Tuple2<String, Integer>>) r ->
                new Tuple2<String, Integer>("success", 1))
                .returns(new TypeHint<Tuple2<String, Integer>>() {
                });

        SingleOutputStreamOperator<Tuple2<String, Integer>> failedTuple = filterFailed.map((MapFunction<PaySuccessRateBean, Tuple2<String, Integer>>) r ->
                new Tuple2<String, Integer>("failed", 1))
                .returns(new TypeHint<Tuple2<String, Integer>>() {
                });

        SingleOutputStreamOperator<Tuple2<String, Integer>> keyResult =
                successTuple.union(failedTuple).keyBy(data -> data.f0)
                        .sum(1);


        keyResult.print("订单支付详情：");

        env.execute("PaySuccessRateTest");
    }




}
