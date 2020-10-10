package com.alibaba.base.sink

/**
 * @author fada.yu
 * @date 2020/10/10 13:38
 * @Description：
 */
class RedisSink {
//  def main(args: Array[String]): Unit = {
//    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
//    env.setParallelism(1)
//    val dataDS: DataStreamSource[WaterSensor] = env.addSource(new MySource)
//
//    // 向Redis中输出内容
//    val conf = new FlinkJedisPoolConfig
//    .Builder()
//      .setHost("hadoop02")
//      .setPort(6379)
//      .build()
//    dataDS.addSink(new RedisSink[String](conf, new RedisMapper[String] {
//      override def getCommandDescription: RedisCommandDescription = {
//        new RedisCommandDescription(RedisCommand.HSET,"sensor")
//      }
//
//      override def getKeyFromData(t: String): String = {
//        t.split(",")(1)
//      }
//
//      override def getValueFromData(t: String): String = {
//        t.split(",")(2)
//      }
//    }))
//
//  }


}
