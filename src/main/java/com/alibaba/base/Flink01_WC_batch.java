package com.alibaba.base;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;


/**
 * @author fada.yu
 * @date 2020/9/30 10:42
 * @Description：
 */
public class Flink01_WC_batch  {
    public static void main(String[] args) throws Exception  {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> lineDs = env.readTextFile("D:\\Personal\\workspace\\dp-flink-workspace-fada\\flink-java\\src\\main\\java\\input\\word.txt");
        FlatMapOperator<String, Tuple2<String, Integer>> wordAndOne = lineDs.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] words = value.split(" ");
                for (String word : words
                ) {
                    out.collect(new Tuple2<String, Integer>(word, 1));
                }

            }
        });
        UnsortedGrouping<Tuple2<String, Integer>> wordAndOneGS = wordAndOne.groupBy(0);
        AggregateOperator<Tuple2<String, Integer>> sum = wordAndOneGS.sum(1);
        sum.print();
        env.setParallelism(1);
        env.execute("wordcount batch");


    }
}
