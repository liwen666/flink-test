package jrx.data.hub.flink.example.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * Desc: sink in PG
 * flink的批量环境目前批次的只支持CSV
 * Created by zhisheng on 2020-03-19 08:36
 */
public class SQLExampleData2PG1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);
        String ddlSource =
                "CREATE TABLE `mysql_src1_cas_info` (\n" +
                        "  `user_id` bigint\n" +
                        ")   WITH (\n" +
                        "   'connector.type' = 'jdbc',\n" +
                        "   'connector.url' = 'jdbc:mysql://10.0.8.10:3306/anytask_dev11',\n" +
                        "   'connector.table' = 'cas_info',\n" +
                        "   'connector.username' = 'any',\n" +
                        "   'connector.password' = 'anywd1234'\n" +
                        ")";
        String sql = "select  user_id from mysql_src1_cas_info ";

        blinkStreamTableEnv.executeSql(ddlSource);
        TableResult tableResult = blinkStreamTableEnv.executeSql(sql);
        tableResult.print();
        blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
    }
}
