package jrx.data.hub.flink.example.function.udf;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import com.riveretech.est.common.utils.FlinkUtils;
/**
 * Desc: sink in PG
 * Created by zhisheng on 2020-03-19 08:36
 */
public class SQLExampleData2PG {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);

blinkStreamTableEnv.createTemporaryFunction("test_fn",TestFunction.class);
        String ddlSource = "\n" +
                "create table mysql_table(\n" +
                "`id` INT,\n" +
                "`k` INT,\n" +
                "`c` STRING,\n" +
                "`pad` STRING,\n" +
                "`remark` STRING,\n" +
                "`create_time` TIMESTAMP(3),\n" +
                "`update_time` TIMESTAMP\n" +
                ")\n" +
                "WITH(\n" +
//                "'connector'='mysql-cdc',\n" +
//                "'hostname'='11.11.1.79',\n" +
//                "'port'='3306',\n" +
//                "'password'='root',\n" +
//                "'username'='root',\n" +
//                "'database-name'='flink_web',\n" +
//                "'table-name'='cdc_test')";

                "            'connector' = 'jdbc',\n" +
                "            'url' = 'jdbc:mysql://11.11.1.79:3306/flink_web',\n" +
                "            'table-name' = 'cdc_test',\n" +
                "            'username' = 'root',\n" +
                "            'password' = 'root'\n)";



        String ddlSink = "create table gp_table(\n" +
                "`create_time` TIMESTAMP,\n" +
                "`update_time` TIMESTAMP,\n" +
                "`c` STRING,\n" +
                "`pad` STRING,\n" +
                "`remark` STRING,\n" +
                "`id` INT,\n" +
                "`k` BIGINT\n" +
                ")\n" +
                "WITH(\n" +
//                "'connector'='greenplum',\n" +
//                "'schema-name'='public',\n" +
//                "'url'='jdbc:postgresql://10.0.8.10:5432/flink_sync',\n" +
//                "'table-name'='function_test',\n" +
//                "'username'='gpadmin',\n" +
//                "'password'='gpadmin')";
                "        'connector' = 'greenplum',\n" +
                "        'sink.buffer-flush.max-rows' = '500',\n" +
                "        'sink.buffer-flush.interval' = '15000',\n" +
                "        'schema-name' = 'public',\n" +
                "        'url' ='jdbc:postgresql://10.0.8.10:5432/flink_sync',\n" +
                "        'table-name' = 'function_test',\n" +
                "        'username' = 'gpadmin',\n" +
                "        'password' = 'gpadmin'\n" +
                "        )";

        String sql = "insert into gp_table select create_time ,\n\tupdate_time ,\n\tc ,\n\tpad ,\n\tremark ,\n\ttest_fn(id) ,\n\tk from mysql_table";

        String test ="select * from mysql_table";
        String gp_table ="select * from gp_table";

        blinkStreamTableEnv.executeSql(ddlSource);
        blinkStreamTableEnv.executeSql(ddlSink);
        blinkStreamTableEnv.executeSql(sql);

//        TableResult tableResult = blinkStreamTableEnv.executeSql(test);
//        tableResult.print();
//
//        TableResult tableResult2 = blinkStreamTableEnv.executeSql(gp_table);
//        tableResult2.print();


        try {
            blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
