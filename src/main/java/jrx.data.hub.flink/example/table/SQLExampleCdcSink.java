package jrx.data.hub.flink.example.table;

import jrx.data.hub.flink.example.cdc.SQLExampleCdcKafka;
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
 * @see SQLExampleCdcKafka
 */

public class SQLExampleCdcSink {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);

//        String sink = "CREATE TABLE sink_table (\n" +
//                "  id INT,\n" +
//                "  k INT,\n" +
//                "  c STRING,\n" +
//                "  pad STRING,\n" +
//                "  `remark` STRING\n" +
//                ") WITH (\n" +
//                "   'connector.type' = 'jdbc',\n" +
//                "   'connector.url' = 'jdbc:mysql://11.11.1.79:3306/anytask_dev11',\n" +
//                "   'connector.table' = 'cas_info',\n" +
//                "   'connector.username' = 'any',\n" +
//                "   'connector.password' = 'anywd1234'\n" +
//                ")";
        String sink = "create table sink_table (\n" +
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

        String ddlSource = "create table source_table (\n" +
                "  id INT,\n" +
                "  k INT,\n" +
                "  c STRING,\n" +
                "  pad STRING,\n" +
                "  remark STRING,\n" +
                "  primary key (id) NOT ENFORCED\n" +
                ") with (\n" +
                " 'connector' = 'kafka',\n" +
                " 'properties.bootstrap.servers' = '11.11.1.79:9092',\n" +
                " 'topic' = 'debezium_test',\n" +
                " 'format' = 'debezium-json',\n" +
                // 最早分区消费
//                " 'scan.startup.mode' = 'earliest-offset',\n" +
                // 最近分区消费
//                " 'scan.startup.mode' = 'latest-offset',\n" +
                // 指定偏移量消费
//                 " 'scan.startup.mode' = 'specific-offsets',\n" +
//                 " 'scan.startup.specific-offsets' = 'partition:0,offset:71',\n" +
                // 指定时间戳
                " 'scan.startup.mode' = 'timestamp',\n" +
                 " 'scan.startup.timestamp-millis' = '1616490682000',\n" +

//                " 'sink.buffer-flush.max-rows' = '1',\n" +
                " 'properties.group.id' = 'CDC_TEST')\n";
        String job = " INSERT INTO sink_table (id,k,c,pad) SELECT id,k,c,pad  FROM source_table\n";

        blinkStreamTableEnv.executeSql(ddlSource);
        blinkStreamTableEnv.executeSql(sink);
        TableResult tableResult = blinkStreamTableEnv.executeSql(job);

        blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
    }
}
