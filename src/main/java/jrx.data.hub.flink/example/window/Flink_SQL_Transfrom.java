package jrx.data.hub.flink.example.window;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

public class Flink_SQL_Transfrom {
    public static void main(String[] args) throws Exception {

        // TODO 1 创建流式环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 1.1 设置并行度
        env.setParallelism(1);
        // 1.2 设置运行模式
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);
        // 1.3 设置CheckPoint
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        //env.setStateBackend(new FsStateBackend("hdfs://hadoop102:8020/demo/checkpoint/FlinkCdc_Mysql"));

        // 1.4 设置 表环境
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();

        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env, settings);
        // TODO 2 读取kafka的数据

//        String ddlSource = "create table source_table (\n" +
//                " id INT,\n" +
//                " name VARCHAR,\n" +
//                " age INT,\n" +
//                " score INT,\n" +
//                " tel VARCHAR,\n" +
//                " address VARCHAR\n" +
//                ") with (\n" +
//                " 'connector' = 'kafka',\n" +
//                " 'properties.bootstrap.servers' = '11.11.1.79:9092',\n" +
//                " 'topic' = 'flink_ceshi_1',\n" +
//                " 'format' = 'debezium-json',\n" +
//                " 'scan.startup.mode' = 'earliest-offset',\n" +
//                " 'properties.group.id' = 'CDC_TEST')\n";
//        String job = " select * from source_table\n";

        String ddlSource = "create table source_table (\n" +
                " id INT,\n" +
                " name VARCHAR,\n" +
                " age INT,\n" +
                " score INT,\n" +
                " tel VARCHAR,\n" +
                " address VARCHAR\n" +
                ") with (\n" +
                " 'connector' = 'jdbc',\n" +
                " 'url' = 'jdbc:mysql://11.11.1.79/flink_cdc?useSSL=false&useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC',\n" +
                " 'username' = 'root',\n" +
                " 'password' = 'root',\n" +
                " 'table-name' = 'student',\n" +
                " 'driver' = 'com.mysql.cj.jdbc.Driver',\n" +
                " 'sink.buffer-flush.interval' = '3s',\n" +
                " 'sink.buffer-flush.max-rows' = '1',\n" +
                " 'sink.max-retries' = '5')\n";
        String job = " select * from source_table\n";
        tableEnvironment.executeSql(ddlSource);
//        Table table = tableEnvironment.sqlQuery(job);
//        TableResult tableResult = tableEnvironment.executeSql(job);
//        tableResult.print();

        Table table = tableEnvironment.sqlQuery(job);
        DataStream<Tuple2<Boolean, Row>> tuple2DataStream = tableEnvironment.toRetractStream(table, Row.class);
        DataStreamSink<Tuple2<Boolean, Row>> print = tuple2DataStream.print();
//        // TODO 3  将读到的数据转化为DataStream
////        DataStream<Row> rowDataStream = tableEnvironment.toAppendStream(table, Row.class);
//        DataStream<Tuple2<Boolean, Row>> tuple2DataStream = tableEnvironment.toRetractStream(table, Row.class);
//
//        // TODO 4 创建临时视图
//        tableEnvironment.createTemporaryView("myTable",tuple2DataStream);
//
//        // TODO 5 SQL 查询
//        TableResult tableResult = tableEnvironment.executeSql(
//                " SELECT * " +
//                        " FROM (" +
//                        "    SELECT *," +
//                        "        ROW_NUMBER() OVER (PARTITION BY id ORDER BY score DESC) as row_num" +
//                        "        FROM myTable）" +
//                        "WHERE rom_num <= 3");
//
//        tableResult.print();
//        tuple2DataStream.executeAndCollect();
//        TableResult tableResult = tableEnvironment.executeSql("select * from source_table where name='十一' ");
//        TableResult tableResult = tableEnvironment.executeSql(" SELECT * " +
//                " FROM (" +
//                "    SELECT *," +
//                "        ROW_NUMBER() OVER (PARTITION BY id ORDER BY score DESC) as row_n" +
//                "        FROM source_table)" +
//                " WHERE row_n <= 3 ");
//        tableResult.print();

        TableResult tableResult = tableEnvironment.executeSql(" SELECT * " +
                " FROM (" +
                "    SELECT *," +
//                "        ROW_NUMBER() OVER (PARTITION BY address ORDER BY score DESC) as row_n" +
                "        ROW_NUMBER() OVER (PARTITION BY id ORDER BY score DESC) as row_n" +
                "        FROM source_table)" +
                " WHERE row_n <= 3 ");
        tableResult.print();


//        Table table1 = tableEnvironment.sqlQuery(" SELECT * " +
//                " FROM (" +
//                "    SELECT *," +
//                "        ROW_NUMBER() OVER (PARTITION BY id ORDER BY score DESC) as row_n" +
//                "        FROM source_table)" +
//                " WHERE row_n <= 3 ");
//        DataStream<Tuple2<Boolean, Row>> tuple2DataStream1 = tableEnvironment.toRetractStream(table1, Row.class);
//        tuple2DataStream1.print();
//        tuple2DataStream1.executeAndCollect();
        tableEnvironment.execute("table test");
    }
}
