package com.alibaba.api.keyedprocessfunction.suanzi;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author :YuFada
 * @date： 2020/12/13 0013 下午 21:38
 * Description：
 */
public class InputDSCommon implements SourceFunction {
    @Override
    public void run(SourceContext sourceContext) throws Exception {

    }

    @Override
    public void cancel() {

    }
}