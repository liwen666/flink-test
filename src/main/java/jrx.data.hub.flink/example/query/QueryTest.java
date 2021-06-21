package jrx.data.hub.flink.example.query;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.runtime.util.JsonUtils;
import org.apache.flink.types.Row;
import org.apache.flink.util.CloseableIterator;
import org.java_websocket.client.WebSocketClient;

/**
 * Desc: sink in PG
 * Created by zhisheng on 2020-03-19 08:36
 */
public class QueryTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);


        String query = "CREATE TABLE test_query (\n" +
                "  `id` INT,\n" +
                "  `k` INT,\n" +
                "  `c` STRING,\n" +
                "  `pad` STRING,\n" +
                "  `remark` STRING,\n" +
//                "  `create_time` TIMESTAMP(3)  WITH LOCAL TIME ZONE METADATA  ,\n" +
//                "  `create_time` TIMESTAMP(3)\n" +
                /**
                 * 消费kafka的数据，字段类型必须匹配才行
                 */
                "  `create_time` BIGINT\n" +
//                "  `update_time` TIMESTAMP(3)\n" +
//                "  `update_time` BIGINT\n" +
                ")\n"+
                "WITH(\n" +
                "'connector'='kafka',\n" +
                "'topic'='flink_web.cdc_test',\n" +
                "'properties.bootstrap.servers'='11.11.1.79:9092',\n" +
                "'properties.group.id'='flink_cdc',\n" +
                "'format'='debezium-json',\n" +
                "'scan.startup.mode'='earliest-offset')";

        blinkStreamTableEnv.executeSql(query);

        blinkStreamTableEnv.createTemporaryFunction(
                "convertDate",
                ConvertDate.class
        );



//        TableResult tableResult = blinkStreamTableEnv.executeSql("select id convertDate(update_time) from test_query");
        TableResult tableResult = blinkStreamTableEnv.executeSql("select id, convertDate(create_time) create_time from test_query");
        TableSchema tableSchema = tableResult.getTableSchema();
        CloseableIterator<Row> collect = tableResult.collect();
        WebSocketClient client = WebSocketTest.getClient();
        while (collect.hasNext()){
            Row next = collect.next();
            System.out.println(next);
            client .send(next.toString());
        }
//        tableResult.print();

        try {
            blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
        } catch (Exception e) {
            System.err.println(e);
        }
        client.close();
    }
}
