package com.alibaba.api.keyedprocessfunction.suanzi;

import com.alibaba.base.MySource;
import com.alibaba.base.bean.WaterSensor;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author :YuFada
 * @date： 2020/12/19 0019 下午 21:18
 * Description：
 */
public class ReduceTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<WaterSensor> sensorDS = env.addSource(new MySource());


        // 2.转换成元组
        SingleOutputStreamOperator<Tuple2<String, Integer>> sensorTuple = sensorDS
                .map((MapFunction<WaterSensor, Tuple2<String, Integer>>) r -> new Tuple2<String, Integer>(r.getId(), r.getVc()))
                .returns(new TypeHint<Tuple2<String, Integer>>() {
                });


        // 3.按照id分组
        KeyedStream<Tuple2<String, Integer>, String> sensorKS = sensorTuple.keyBy(r -> r.f0);

        // 4.聚合
        SingleOutputStreamOperator<Tuple2<String, Integer>> resultDS = sensorKS.reduce((v1, v2) -> new Tuple2<String, Integer>(v1.f0, v1.f1 + v2.f1));

        resultDS.print("reduce");

        env.execute();
    }
}