package com.alibaba.job.market;

import com.alibaba.job.bean.MarketingUserBehavior;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author fada.yu
 * @date 2020/10/15 10:06
 * @Description：
 */
public class AppMarketingByChannel {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<MarketingUserBehavior> inputDS = env.addSource(new AppSourceFunction());
        SingleOutputStreamOperator<MarketingUserBehavior> filterDS = inputDS.filter(new FilterFunction<MarketingUserBehavior>() {
            @Override
            public boolean filter(MarketingUserBehavior value) throws Exception {
                return "HUAWEI".equals(value.getChannel()) || "XIAOMI".equals(value.getChannel());
            }
        });
        SingleOutputStreamOperator<Tuple2<String, Integer>> kvDS = filterDS.map(new MapFunction<MarketingUserBehavior, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(MarketingUserBehavior value) throws Exception {
                return Tuple2.of(value.getChannel() + "_" + value.getBehavior(), 1);
            }
        });
        SingleOutputStreamOperator<Tuple2<String, Integer>> keydStream = kvDS.keyBy(data -> data.f0)
                .sum(1);
        keydStream.print("AppMarketingByChannel");

        env.execute("Marketing ");


    }

    public static class MyKeyedProcessFunction extends KeyedProcessFunction<String, Tuple2<String, Integer>, String> {

        @Override
        public void processElement(Tuple2<String, Integer> value, Context ctx, Collector<String> out) throws Exception {
            out.collect("key = " + ctx.getCurrentKey() + ",数据 = " + value);
        }

    }

        public static class AppSourceFunction implements SourceFunction<MarketingUserBehavior>{
        private boolean flag =true;
        private List<String> userBehaviorList= Arrays.asList("DOWNLOAD","INSTALL","UPDATE","UNINSTALL");
        private List<String>  channelList= Arrays.asList("HUAWEI","XIAOMI","OPPO","VIVO");

        @Override
        public void run(SourceContext<MarketingUserBehavior> sourceContext) throws Exception {
            Random random = new Random();
            while (flag){
                sourceContext.collect(
                        new MarketingUserBehavior(random.nextLong(),
                                userBehaviorList.get(random.nextInt(userBehaviorList.size())),
                                channelList.get(random.nextInt(channelList.size())),
                                        System.currentTimeMillis())
                );
                        Thread.sleep(1000L);

            }

        }

        @Override
        public void cancel() {
            flag=false;

        }
    }
}
