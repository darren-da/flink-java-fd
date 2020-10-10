package com.alibaba.base;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;


/**
 * @author fada.yu
 * @date 2020/9/30 11:34
 * @Description：
 */
public class Flink03_WC_UnBoundedStream {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> socketDs = env.socketTextStream("localhost", 9999);
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordAndOneTuple = socketDs.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] words = value.split(" ");
                for (String word :
                        words) {

                    out.collect(new Tuple2<String, Integer>(word, 1));
                }
            }
        });
        KeyedStream<Tuple2<String, Integer>, Tuple> wordAndOneKs = wordAndOneTuple.keyBy(0);
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = wordAndOneKs.sum(1);
        sum.print();
        env.setParallelism(1);
        env.execute("stream job first");
    }
}
