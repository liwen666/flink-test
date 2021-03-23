package jrx.data.hub.flink.example.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.ScalarFunction;

/**
 * Desc: sink in PG
 * Created by zhisheng on 2020-03-19 08:36
 */
public class SQLExampleData2PGFunction {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);

        String ddlSource = "CREATE TABLE user_behavior (\n" +
                "    score INT" +
                ") WITH (\n" +
                "    'connector.type' = 'kafka',\n" +
                "    'connector.version' = 'universal',\n" +
                "    'connector.topic' = 'user_behavior',\n" +
                "    'connector.startup-mode' = 'earliest-offset',\n" +
                "    'connector.properties.group.id' = 'testGroup',\n" +
                "    'connector.properties.zookeeper.connect' = '11.11.1.79:2181',\n" +
                "    'connector.properties.bootstrap.servers' = '11.11.1.79:9092',\n" +
                "    'format.type' = 'json'\n" +
                ")";

        String ddlSink = "CREATE TABLE user_behavior_aggregate (\n" +
                "    score numeric(38, 18)\n" +
                ") WITH (\n" +
                "    'connector.type' = 'jdbc',\n" +
                "    'connector.driver' = 'org.postgresql.Driver',\n" +
                "    'connector.url' = 'jdbc:postgresql://172.16.103.105:5432/any_data_test?reWriteBatchedInserts=true',\n" +
                "    'connector.table' = 'user_behavior', \n" +
                "    'connector.username' = 'gpadmin', \n" +
                "    'connector.password' = 'gpadmin',\n" +
                "    'connector.write.flush.max-rows' = '1' \n" +
                ")";
        //测试函数

        blinkStreamTableEnv.createTemporaryFunction(
                "max2",
                Max2Function3.class
        );
        String sql = "insert into user_behavior_aggregate select max2(score) from user_behavior";

        blinkStreamTableEnv.executeSql(ddlSource);
        blinkStreamTableEnv.executeSql(ddlSink);
        blinkStreamTableEnv.executeSql(sql);


        TableResult tableResult2 = blinkStreamTableEnv.executeSql("select max2(score) from user_behavior");
        tableResult2.print();

        try {
            blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * 比较2个数最大值
     *
     */
    public static class Max2Function3 extends ScalarFunction {
        /**
         */
        public Integer eval(Integer num1) {
            return num1 + 2;
        }

    }

}
