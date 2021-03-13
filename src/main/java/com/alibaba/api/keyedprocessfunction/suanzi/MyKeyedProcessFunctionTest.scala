package com.alibaba.api.keyedprocessfunction.suanzi

import java.sql.Timestamp

import com.alibaba.base.bean.WaterSensors
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.util.Collector

/**
  * @author :YuFada
  * @date ： 2021/3/13 0013 上午 9:29
  *       Description：
  *       自定义数据处理函数
  */
object MyKeyedProcessFunctionTest {
    def main(args: Array[String]): Unit = {


    }


    class MyKeyedProcessFunction extends KeyedProcessFunction[String, WaterSensors, String] {
        private var currentHeight = 0L
        private var alarmTimer = 0L



        override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, WaterSensors, String]#OnTimerContext, out: Collector[String]): Unit = {
            out.collect("水位传感器" + ctx.getCurrentKey + "在 " + new Timestamp(timestamp) + "已经连续5s水位上涨。。。")
        }

        // 分区中每来一条数据就会调用processElement方法
        override def processElement(value: WaterSensors, ctx: KeyedProcessFunction[String, WaterSensors, String]#Context, out: Collector[String]): Unit = {

            // 判断当前水位值和之前记录的水位值的变化
            if (value.vc > currentHeight) {
                // 当水位值上升的时候，开始计算时间，如果到达5s，
                // 中间水位没有下降，那么定时器应该执行
                if (alarmTimer == 0) {
                    alarmTimer = value.ts * 1000 + 5000
                    ctx.timerService().registerEventTimeTimer(alarmTimer)
                }
            } else {
                // 水位下降的场合
                // 删除定时器处理
                ctx.timerService().deleteEventTimeTimer(alarmTimer)
                alarmTimer = 0L
            }

            // 保存当前水位值
            currentHeight=value.vc.byteValue()
        }

    }

}