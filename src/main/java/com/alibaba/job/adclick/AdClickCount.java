package com.alibaba.job.adclick;

import com.alibaba.job.bean.AdClickLog;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.executiongraph.Execution;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author fada.yu
 * @date 2020/10/16 11:10
 * @Description：
 */
public class AdClickCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<AdClickLog> adClickDS = env.readTextFile("input/AdClickLog.csv")
                .map(new MapFunction<String, AdClickLog>() {
                    @Override
                    public AdClickLog map(String value) throws Exception {
                        String[] datas = value.split(",");
                        return new AdClickLog(
                                Long.valueOf(datas[0]),
                                Long.valueOf(datas[1]),
                                datas[2],
                                datas[3],
                                Long.valueOf(datas[4])
                        );
                    }
                });

        SingleOutputStreamOperator<AdClickLog> filterDS = adClickDS.filter(data -> data.getProvince().equals("beijing")
        ||data.getProvince().equals("nanjing"));
//        SingleOutputStreamOperator<AdClickLog> filterDS = adClickDS.filter(new FilterFunction<AdClickLog>() {
//            @Override
//            public boolean filter(AdClickLog value) throws Exception {
//                return "hangzhou".equals(value.getProvince()) || "nanjing".equals(value.getProvince());
//            }
//        });


        SingleOutputStreamOperator<Tuple2<String, Integer>> adClickCountDs = filterDS.map(new MapFunction<AdClickLog, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(AdClickLog value) throws Exception {
                return Tuple2.of(value.getProvince() + "_" + value.getAdId(), 1);
            }
        })
                .keyBy(data -> data.f0)
                .sum(1);

        adClickCountDs.print("addClickCount");
   env.execute("addclickCount");
    }

}
