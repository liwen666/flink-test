package jrx.data.hub.flink.example.table;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.apache.flink.util.CloseableIterator;

import java.util.List;

/**
 * Desc: sink in PG
 * Created by zhisheng on 2020-03-19 08:36
 */
public class SQLExampleData2PGDataStream {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);


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
                " 'topic' = 'route1',\n" +
                " 'format' = 'debezium-json',\n" +
                // 最早分区消费
                " 'scan.startup.mode' = 'earliest-offset',\n" +
                // 最近分区消费
//                " 'scan.startup.mode' = 'latest-offset',\n" +
                // 指定偏移量消费
//                 " 'scan.startup.mode' = 'specific-offsets',\n" +
//                 " 'scan.startup.specific-offsets' = 'partition:0,offset:71',\n" +
                // 指定时间戳
//                " 'scan.startup.mode' = 'timestamp',\n" +
//                " 'scan.startup.timestamp-millis' = '1616490682000',\n" +

//                " 'sink.buffer-flush.max-rows' = '1',\n" +
                " 'properties.group.id' = 'CDC_TEST')\n";

//
        String sql = "select * from source_table";

        blinkStreamTableEnv.executeSql(ddlSource);
//        TableResult tableResult = blinkStreamTableEnv.executeSql(sql);
//        CloseableIterator<Row> collect = tableResult.collect();
//        blinkStreamEnv.fromCollection(collect);
//        DataStreamSource
        Table table = blinkStreamTableEnv.sqlQuery(sql);
//        DataStream<Row> resultStream = blinkStreamTableEnv.toAppendStream(table,Row.class);
        DataStream<Tuple2<Boolean, Row>> tuple2DataStream = blinkStreamTableEnv.toRetractStream(table, Row.class);
        List<Tuple2<Boolean, Row>> ccc = tuple2DataStream.executeAndCollect("ccc", 1);
//        ccc.print();

        try {
            blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
