package com.alibaba.job.demo;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author fada.yu
 * @version 1.0
 * @date 2022/3/7 11:10
 * @Desc:
 */
public class MySourceSuccesRate implements SourceFunction<PaySuccessRateBean> {
    public static Object PaySuccessRate;
    private boolean flag=true;
    private Long ts=System.currentTimeMillis();
    private List<String>  channelList= Arrays.asList("HUAWEI","XIAOMI","OPPO","VIVO","APPLE");
    private List<Long> startTimeList=Arrays.asList(ts-1000L,ts-2000L,ts-3000L);
    private List<Long> endTimeList=Arrays.asList(ts+1000L,ts+2000L,ts+3000L,0L);
    private List<Integer> codeList=Arrays.asList(4019,5020,5021,5029,4017,4020);
    private List<String> vcList=Arrays.asList("1.0","2.0","3.0");
    @Override
    public void run(SourceContext sourceContext) throws Exception {
        Random random = new Random();
        while (flag) {

            sourceContext.collect(new PaySuccessRateBean(
                    DigestUtils.md5Hex(String.valueOf(random.nextInt()*9)).substring(0,12),
                    Math.abs(random.nextInt()),
                    startTimeList.get(random.nextInt(startTimeList.size())),
                    endTimeList.get(random.nextInt(endTimeList.size())),
                    channelList.get(random.nextInt(channelList.size())),
                    codeList.get(random.nextInt(codeList.size())),
                    codeList.get(random.nextInt(codeList.size())),
                    codeList.get(random.nextInt(codeList.size())),
                    ts,
                    vcList.get(random.nextInt(vcList.size()))
                    )
            );
            Thread.sleep(200L);
        }
    }

    @Override
    public void cancel() {
        flag=false;

    }


}
