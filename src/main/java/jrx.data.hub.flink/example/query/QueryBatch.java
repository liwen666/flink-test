package jrx.data.hub.flink.example.query;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.apache.flink.util.CloseableIterator;
import org.java_websocket.client.WebSocketClient;

/**
 * Desc: sink in PG
 * Created by zhisheng on 2020-03-19 08:36
 */
public class QueryBatch {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);

        String query = "create table batch_test (\n" +
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

        blinkStreamTableEnv.executeSql(query);

        blinkStreamTableEnv.createTemporaryFunction(
                "convertDate",
                ConvertDate.class
        );



//        TableResult tableResult = blinkStreamTableEnv.executeSql("select id convertDate(update_time) from test_query");
        TableResult tableResult = blinkStreamTableEnv.executeSql("select * from batch_test");
        TableSchema tableSchema = tableResult.getTableSchema();
        CloseableIterator<Row> collect = tableResult.collect();
        WebSocketClient client = WebSocketTest.getClient();
        while (collect.hasNext()){
            Row next = collect.next();
            System.out.println(next);
            client .send(next.toString());
        }
        tableResult.print();

        try {
            blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
        } catch (Exception e) {
            System.err.println(e);
        }
        client.close();
    }
}
