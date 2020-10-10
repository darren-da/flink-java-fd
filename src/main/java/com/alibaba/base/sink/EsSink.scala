package com.alibaba.base.sink

import java.util

import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.elasticsearch.{ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.apache.http.HttpHost
import org.elasticsearch.client.Requests

/**
 * @author fada.yu
 * @date 2020/10/10 13:40
 * @Description：
 */
class EsSink {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val dataDS: DataStreamSource[WaterSensor] = env.addSource(new MySource)
    val httpHosts = new util.ArrayList[HttpHost]()
    httpHosts.add(new HttpHost("hadoop02", 9200))
//
//    val ds: DataStream[WaterSensor] = dataDS.map(
//      s => {
//        val datas = s.split(",")
//        WaterSensor(datas(0), datas(1).toLong, datas(2).toDouble)
//      }
//    )

    val esSinkBuilder = new ElasticsearchSink.Builder[WaterSensor]( httpHosts, new ElasticsearchSinkFunction[WaterSensor] {
      override def process(t: WaterSensor, runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
        println("saving data: " + t)
        val json = new util.HashMap[String, String]()
        json.put("data", t.toString)
        val indexRequest = Requests.indexRequest().index("sensor").`type`("readingData").source(json)
        requestIndexer.add(indexRequest)
        println("saved successfully")
      }
    } )
    // 启动ES时。请使用es用户
    // 访问路径：http://hadoop02:9200/_cat/indices?v
    // 访问路径：http://hadoop02:9200/sensor/_search
//    ds.addSink( esSinkBuilder.build() )

  }

}
