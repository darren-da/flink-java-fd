/*
package com.alibaba.base.sink

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}


/**
  * @author fada.yu
  * @date 2020/10/10 13:05
  * @Description ：
  */


class MyJdbcSink() extends RichSinkFunction[String]{
  var conn: Connection = _
  var insertStmt: PreparedStatement = _
  //var updateStmt: PreparedStatement = _

  // open 主要是创建连接
  override def open(parameters: Configuration): Unit = {
    super.open(parameters)

    conn = DriverManager.getConnection("jdbc:mysql://hadoop01:3306/rdd", "root", "000000")
    insertStmt = conn.prepareStatement("INSERT INTO user (id, name, age) VALUES (?, ?, ?)")
    //updateStmt = conn.prepareStatement("UPDATE temperatures SET temp = ? WHERE sensor = ?")
  }
  // 调用连接，执行sql
  override def invoke(value: String, context: SinkFunction.Context[_]): Unit = {

    insertStmt.setInt(1, 1)
    insertStmt.setString(2, "zzz")
    insertStmt.setInt(3, 10)
    insertStmt.execute()
  }

  override def close(): Unit = {
    insertStmt.close()
    conn.close()
  }
}

*/
