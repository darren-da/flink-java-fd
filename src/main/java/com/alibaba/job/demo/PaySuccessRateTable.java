package com.alibaba.job.demo;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author fada.yu
 * @version 1.0
 * @date 2022/3/7 16:31
 * @Desc:
 */
public class PaySuccessRateTable {
    public static void main(String[] args) throws Exception {
        //TODO 0.基本环境准备
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置并行度
        env.setParallelism(4);
        DataStreamSource<PaySuccessRateBean> inputStream = env.addSource(new MySourceSuccesRate());

        /*
        //CK相关设置
        env.enableCheckpointing(5000, CheckpointingMode.AT_LEAST_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        StateBackend fsStateBackend = new FsStateBackend(
                "hdfs://hadoop202:8020/gmall/flink/checkpoint/ProvinceStatsSqlApp");
        env.setStateBackend(fsStateBackend);
        System.setProperty("HADOOP_USER_NAME","atguigu");
        */

        // TODO: 2021/12/7  1.定义Table流环境
        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode()
                .build();

        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env, settings);

       tableEnvironment.executeSql("CREATE TABLE source_table (  " +
                "channel STRING," +
                "pageOpen int," +
                "payEnd bigint," +
                "payId STRING," +
                "payStart bigint," +
                "referCode int," +
                "srcCode int," +
                "ts bigint," +
                "userId int," +
                "vc String) " +
                "WITH (  'connector.type' = 'filesystem',  'connector.path' = 'D:\\DevelopWorkspace2\\Flink\\flink-java-fd\\src\\main\\resources\\input\\PaySuccessRate.csv','format.type' = 'csv',  'format.field-delimiter' = ',',  'format.line-delimiter' = '\\r',  'format.ignore-first-line' = 'true',  'format.ignore-parse-errors' = 'true')");

        tableEnvironment.executeSql("create table pipline_sink(  " +
                "channel STRING," +
                "pageOpen int," +
                "payEnd bigint," +
                "payId STRING," +
                "payStart bigint," +
                "referCode int," +
                "srcCode int," +
                "ts bigint," +
                "userId int," +
                "vc String) " +
                "with ('connector' = 'print'" +
                ")");

        //tableEnvironment.executeSql("insert into pipline_sink select context from source_table");

        TableResult tableResult = tableEnvironment.executeSql("insert into pipline_sink select * from source_table ");

        tableResult.print();
        env.execute("test-job");


        //Table table = tableEnvironment.fromDataStream(inputStream);
        ////table.printSchema();
        ////root
        ////        |-- channel: STRING
        ////        |-- pageOpen: INT
        ////        |-- payEnd: BIGINT
        ////        |-- payId: STRING
        ////        |-- payStart: BIGINT
        ////        |-- referCode: INT
        ////        |-- srcCode: INT
        ////        |-- ts: BIGINT
        ////        |-- userId: INT
        ////        |-- vc: STRING
        //
        //
        //tableEnvironment.createTemporaryView("payrate",table);
        //Table select = tableEnvironment.sqlQuery("select * from payrate");
        ////select.printSchema();
        //
        //DataStream<PaySuccessRateTable> paySuccessRateTableDataStream = tableEnvironment.toAppendStream(select, PaySuccessRateTable.class);
        ////
        //paySuccessRateTableDataStream.print();
        //env.execute("test pay");


        //
        ////TODO 2.把数据源定义为动态表
        //
        //
        //tableEnv.executeSql("CREATE TABLE ORDER_WIDE (province_id BIGINT, " +
        //        "province_name STRING,province_area_code STRING" +
        //        ",province_iso_code STRING,province_3166_2_code STRING,order_id STRING, " +
        //        "split_total_amount DOUBLE,create_time STRING,rowtime AS TO_TIMESTAMP(create_time) ," +
        //        "WATERMARK FOR  rowtime  AS rowtime)" +
        //        " WITH (" + MyKafkaUtil.getKafkaDDL(orderWideTopic, groupId) + ")");
        //
        ////TODO 3.聚合计算
        //Table provinceStateTable = tableEnv.sqlQuery("select " +
        //        "DATE_FORMAT(TUMBLE_START(rowtime, INTERVAL '10' SECOND ),'yyyy-MM-dd HH:mm:ss') stt, " +
        //        "DATE_FORMAT(TUMBLE_END(rowtime, INTERVAL '10' SECOND ),'yyyy-MM-dd HH:mm:ss') edt , " +
        //        " province_id,province_name,province_area_code area_code," +
        //        "province_iso_code iso_code ,province_3166_2_code iso_3166_2 ," +
        //        "COUNT( DISTINCT  order_id) order_count, sum(split_total_amount) order_amount," +
        //        "UNIX_TIMESTAMP()*1000 ts "+
        //        " from  ORDER_WIDE group by  TUMBLE(rowtime, INTERVAL '10' SECOND )," +
        //        " province_id,province_name,province_area_code,province_iso_code,province_3166_2_code ");
        //
        //
        ////TODO 4.转换为数据流
        //DataStream<ProvinceStats> provinceStatsDataStream =
        //        tableEnv.toAppendStream(provinceStateTable, ProvinceStats.class);
        //
        ////TODO 5.写入到lickHouse
        ////provinceStatsDataStream.addSink(ClickhouseUtil.
        ////        <ProvinceStats>getJdbcSink("insert into  province_stats_2021  values(?,?,?,?,?,?,?,?,?,?)"));

        //provinceStatsDataStream.print();

        //env.execute("table-sql");

    }

}
