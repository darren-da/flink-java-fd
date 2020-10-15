package com.alibaba.job.market;

import com.alibaba.job.bean.MarketingUserBehavior;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author fada.yu
 * @date 2020/10/15 10:06
 * @Description：
 */
public class AppMarketing {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<MarketingUserBehavior> inputDS = env.addSource(new AppSourceFunction());


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
