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
 * @date 2020/9/30 11:18
 * @Description：
 */
public class Flink02_WC_BoundedStream {
    public static void main(String[] args) throws Exception {
        // 0.创建流式执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        // 1.读取文件
        DataStreamSource<String> fileDS = env.readTextFile("D:\\Personal\\workspace\\dp-flink-workspace-fada\\flink-java\\src\\main\\java\\input\\word.txt");

        // 2.转换数据格式
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordAndOneTuple = fileDS.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
//                JSONObject jsonObjectSrc = JSON.parseObject(value);

                String[] words = value.split(" ");
                for (String word : words) {
                    out.collect(new Tuple2<String, Integer>(word, 1));
                }
            }
        });

        // 3.分组
        KeyedStream<Tuple2<String, Integer>, Tuple> wordAndOneKS = wordAndOneTuple.keyBy(0);

        // 4.求和
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = wordAndOneKS.sum(1);

        // 5.打印
        sum.print();

        // 6.执行
        env.execute();
    }

}
