package jrx.data.hub.flink.example.file;

import com.alibaba.fastjson.JSON;
import org.apache.flink.configuration.PipelineOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.StatementSet;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * Desc: sink in PG
 * Created by zhisheng on 2020-03-19 08:36
 */
public class KafkaToFile {
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
                "'topic'='flink_web.job_config',\n" +
                "'properties.bootstrap.servers'='11.11.1.79:9092',\n" +
                "'properties.group.id'='ODS_T_Group1',\n" +
                "'value.format'='raw',\n" +
                "'scan.startup.mode'='earliest-offset')";


        String ddlSink = "CREATE TABLE file_binlog_dml(\n" +
                "            binlog string,\n" +
                "            tb string,\n" +
                "            biz_date string\n" +
                "        )\n" +
                "PARTITIONED BY (tb,biz_date)\n" +
                "WITH(\n" +
                "'connector'='filesystem',\n" +
                "'path'='file:///D:\\flink12\\flink-1.12.1-src\\flink-engine\\src\\main\\java\\jrx.data.hub.flink\\example\\file\\test',\n" +
                "'sink.rolling-policy.file-size'='128MB',\n" +
                "'sink.rolling-policy.rollover-interval'='1 min',\n" +
                "'sink.rolling-policy.check-interval'='1 min',\n" +
                "'format'='raw',\n" +
                "'sink.partition-commit.policy.kind'='success-file,custom',\n" +
                "'sink.partition-commit.policy.class'='jrx.data.hub.flink.example.file.MergeComitPolicy',\n" +
                "'sink.partition-commit.success-file.name'='_SUCCESS')";

        String sql = "INSERT INTO file_binlog_dml\n        SELECT\n            binlog,\n            get_source_meta(binlog,'table') AS tb,\n            FROM_UNIXTIME(CAST(SUBSTR(get_source_meta(binlog,'ts_ms'),1,10) AS BIGINT),'yyyy-MM-dd') AS biz_date\n        FROM kafka_binlog_dml\n        WHERE binlog is not null";


        blinkStreamTableEnv.executeSql(ddlSource);
        blinkStreamTableEnv.executeSql(ddlSink);
        blinkStreamTableEnv.executeSql(sql);
        String query = "select * from kafka_binlog_dml";
        blinkStreamTableEnv.getConfig().getConfiguration().setString(PipelineOptions.NAME.key(), "-------------test---------------");

        StatementSet stmtSet = blinkStreamTableEnv.createStatementSet();
        stmtSet.addInsertSql(sql);
        TableResult tableResult = stmtSet.execute();
        System.out.println("Flink Stream Job 提交完成。JobName={}, JobID={}" +
                tableResult.getJobClient().get().getJobID());
        System.out.println(JSON.toJSONString(tableResult));

    }
}
