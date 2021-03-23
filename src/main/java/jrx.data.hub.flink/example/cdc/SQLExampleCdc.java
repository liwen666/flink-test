package jrx.data.hub.flink.example.cdc;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * <p>
 *  描述
 * </p>
 *
 * @author lw
 * @since  2021/3/16 11:47
 */

public class SQLExampleCdc {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);

        String ddlSource = "CREATE TABLE sbtest1 (\n" +
                "  id INT,\n" +
                "  k INT,\n" +
                "  c STRING,\n" +
                "  pad STRING\n" +
                ") WITH (\n" +
                "  'connector' = 'mysql-cdc',\n" +
                "  'hostname' = '11.11.1.79',\n" +
                "  'port' = '3306',\n" +
                "  'username' = 'root',\n" +
                "  'password' = 'root',\n" +
                "  'database-name' = 'flink_web',\n" +
                "  'table-name' = 'cdc_test',\n" +
                "  'debezium.snapshot.mode' = 'initial'\n" +
                ")";

        String sink = "create table printSinkTable (\n" +
                "  id INT,\n" +
                "  k INT,\n" +
                "  c STRING,\n" +
                "  pad STRING,\n" +
                "  primary key (id) NOT ENFORCED\n" +
                ") with (\n" +
                " 'connector' = 'jdbc',\n" +
                " 'url' = 'jdbc:mysql://11.11.1.79/mydb?useSSL=false&useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC',\n" +
                " 'username' = 'root',\n" +
                " 'password' = 'root',\n" +
                " 'table-name' = 'sink_test',\n" +
                " 'driver' = 'com.mysql.cj.jdbc.Driver',\n" +
                " 'sink.buffer-flush.interval' = '3s',\n" +
                " 'sink.buffer-flush.max-rows' = '1',\n" +
                " 'sink.max-retries' = '5')\n";
        String job = " INSERT INTO printSinkTable SELECT * FROM sbtest1\n";

        blinkStreamTableEnv.executeSql(ddlSource);
        blinkStreamTableEnv.executeSql(sink);
        TableResult tableResult = blinkStreamTableEnv.executeSql(job);

        blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
    }
    /*---------------------------------------------------------------------------------/
    到 kafka
     CREATE TABLE kafka_gmv (
              id INT,
              k INT,
              c STRING,
              pad STRING
            ) WITH (
                'connector' = 'kafka-0.11',
                'topic' = 'kafka_gmv',
                'scan.startup.mode' = 'earliest-offset',
                'properties.bootstrap.servers' = '197.XXX.XXX.XXX:9092',
                'format' = 'changelog-json'
            );
            INSERT INTO kafka_gmv SELECT * FROM sbtest1;

    /---------------------------------------------------------------------------------*/
}
