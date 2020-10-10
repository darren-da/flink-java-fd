package com.alibaba.base;

import com.alibaba.base.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;


/**
 * @author fada.yu
 * @date 2020/10/9 18:14
 * @Description：
 */
public class Flink_Reduce01 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<WaterSensor> sensorDS = env.addSource(new MySource());
        SingleOutputStreamOperator<Tuple2<String, Integer>> sensorTuple = sensorDS.map((MapFunction<WaterSensor, Tuple2<String, Integer>>) r ->
                new Tuple2<String, Integer>(r.getId(), r.getVc()))
                .returns(new TypeHint<Tuple2<String, Integer>>() {
                });

        KeyedStream<Tuple2<String, Integer>, String> sensorKS = sensorTuple.keyBy(r -> r.f0);
        SingleOutputStreamOperator<Tuple2<String, Integer>> resultDS = sensorKS.reduce((v1, v2) -> new Tuple2<String, Integer>(v1.f0, v1.f1 + v2.f1));

        resultDS.print("reduce");
        env.execute("first reduce job");

    }


    public static class MyKeyedProcessFunction extends KeyedProcessFunction<String, Tuple2<String, Integer>, String> {

        @Override
        public void processElement(Tuple2<String, Integer> value, Context ctx, Collector<String> out) throws Exception {
            out.collect("key = " + ctx.getCurrentKey() + ",数据 = " + value);
        }


    }


//    public DataStreamSink<T> print() {
//        PrintSinkFunction<T> printFunction = new PrintSinkFunction<>();
//        return addSink(printFunction).name("Print to Std. Out");
//    }

}
