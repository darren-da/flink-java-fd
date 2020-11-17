package com.alibaba.api.keyedprocessfunction

import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

/**
 * @author fada.yu
 * @date 2020/11/4 19:53
 * @Description：
 */
class KeyedProcessFunction {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val dataDS: DataStreamSource[String] = env.readTextFile("input/word.txt")


  }
}
