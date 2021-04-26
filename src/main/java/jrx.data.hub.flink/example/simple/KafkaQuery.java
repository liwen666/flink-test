package jrx.data.hub.flink.example.simple;

import jrx.data.hub.flink.example.file.GetSourceMeta;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * Desc: sink in PG
 * Created by zhisheng on 2020-03-19 08:36
 */
public class KafkaQuery {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);
        blinkStreamTableEnv.createTemporaryFunction("get_source_meta", GetSourceMeta.class);

        String ddlSource = " CREATE TABLE kafka_binlog_dml(\n" +
                "            binlog string\n" +
                "        )\n" +
                "WITH(\n" +
                "'connector'='kafka',\n" +
                "'topic'='transactions',\n" +
                "'properties.bootstrap.servers'='11.11.1.79:9092',\n" +
                "'properties.group.id'='ODS_T_Group1',\n" +
                "'value.format'='raw',\n" +
                "'scan.startup.mode'='earliest-offset')";


        blinkStreamTableEnv.executeSql(ddlSource);
        String query = "select * from kafka_binlog_dml";
        TableResult tableResult = blinkStreamTableEnv.executeSql(query);
        System.out.println("-----------------------------------------------------------------------");
        tableResult.print();


        try {
            blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
