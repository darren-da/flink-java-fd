package com.alibaba.job.market;

import com.alibaba.job.bean.MarketingUserBehavior;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author fada.yu
 * @date 2020/10/20 10:05
 * @Description：
 */
public class UserMarketing {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<MarketingUserBehavior> inputDS = env.addSource(new AppMarketingByChannel.AppSourceFunction());
        SingleOutputStreamOperator<MarketingUserBehavior> filterDS = inputDS.filter(new FilterFunction<MarketingUserBehavior>() {
            @Override
            public boolean filter(MarketingUserBehavior value) throws Exception {
                return "DOWNLOAD".equals(value.getBehavior())||"INSTALL".equals(value.getBehavior()) ;
            }
        });
        SingleOutputStreamOperator<Tuple2<String, Integer>> kvDS = filterDS.map(new MapFunction<MarketingUserBehavior, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(MarketingUserBehavior value) throws Exception {
                return Tuple2.of(value.getBehavior(), 1);
            }
        });

        SingleOutputStreamOperator<Tuple2<String, Integer>> keySum = kvDS.keyBy(data -> data.f0).sum(1);
        keySum.print("appMarketing");
        env.execute("appMarketing");


    }
}
