package jrx.data.hub.flink.example.cdc.simple;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
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

public class SQLExampleCdcKafkaCheckPoint {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(100000L); //5s执行一次Checkpoint
        // 设置Checkpoint的模式：精准一次
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // 确保检查点之间有至少1s的间隔【checkpoint最小间隔】
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
        // 检查点必须在一分钟内完成，或者被丢弃【checkpoint的超时时间】
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        // 同一时间只允许进行一个检查点
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //任务挂掉的时候是否清理checkpoint。使任务正常退出时不删除CK内容，有助于任务恢复。默认的是取消的时候清空checkpoint中的数据。RETAIN_ON_CANCELLATION表示取消任务的时候，保存最后一次的checkpoint。便于任务的重启和恢复，正常情况下都使用RETAIN
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//        //设置一个重启策略：默认的固定延时重启次数，重启的次数是Integer的最大值，重启的间隔是1s
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(500, 10000L));
        env.setStateBackend(new FsStateBackend("file:\\C:\\Users\\liwen\\Desktop\\backend\\check"));
        env.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(env, blinkStreamSettings);
        String ddlSource = "CREATE TABLE sbtest1 (\n" +
                "  id INT,\n" +
                "  k INT,\n" +
                "  c STRING,\n" +
                "  pad STRING,\n" +
                "  `remark` STRING\n" +
//                "  ts_ms BIGINT\n" +

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
        String ddlSource2 = "CREATE TABLE data_info (\n" +
                "  name STRING,\n" +
                "  remark String\n" +
                ") WITH (\n" +
                "  'connector' = 'mysql-cdc',\n" +
                "  'hostname' = '11.11.1.79',\n" +
                "  'port' = '3306',\n" +
                "  'username' = 'root',\n" +
                "  'password' = 'root',\n" +
                "  'database-name' = 'flink_web',\n" +
                "  'table-name' = 'data_info',\n" +
                "  'debezium.snapshot.mode' = 'initial'\n" +
                ")";

        String sink = "create table printSinkTable (\n" +
                "  id INT,\n" +
                "  k INT,\n" +
                "  c STRING,\n" +
                "  pad STRING,\n" +

                "  remark STRING\n" +
//                "  ts_ms BIGINT\n" +

                ") " +
                "with (\n" +
                " 'connector' = 'kafka',\n" +
                " 'properties.bootstrap.servers' = '11.11.1.79:9092',\n" +
                " 'topic' = 'bug_test',\n" +
                " 'format' = 'debezium-json',\n" +
                " 'scan.startup.mode' = 'earliest-offset',\n" +
//                " 'sink.buffer-flush.max-rows' = '1',\n" +
                " 'properties.group.id' = 'CDC_TEST')\n";

        String sink2 = "create table printSinkTable2 (\n" +
                "  name STRING,\n" +
                "  remark String\n" +
                ") with (\n" +
                " 'connector' = 'kafka',\n" +
                " 'properties.bootstrap.servers' = '11.11.1.79:9092',\n" +
                " 'topic' = 'bug_test2',\n" +
                " 'format' = 'debezium-json',\n" +
                " 'scan.startup.mode' = 'earliest-offset',\n" +
//                " 'sink.buffer-flush.max-rows' = '1',\n" +
                " 'properties.group.id' = 'CDC_TEST')\n";
        String job = " INSERT INTO printSinkTable(id,k,c,pad) SELECT id,k,c,pad,remark FROM sbtest1\n";
        String job2 = " INSERT INTO printSinkTable2  SELECT *  FROM data_info\n";

        blinkStreamTableEnv.executeSql(ddlSource);
        blinkStreamTableEnv.executeSql(ddlSource2);
        blinkStreamTableEnv.executeSql(sink);
        blinkStreamTableEnv.executeSql(sink2);
        TableResult tableResult = blinkStreamTableEnv.executeSql(job);
        TableResult tableResult2 = blinkStreamTableEnv.executeSql(job2);

        blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
    }
}
