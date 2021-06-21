package jrx.data.hub.flink.datahub;

import jrx.data.hub.flink.example.cdc.multiple.FlinkCdc;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author lw
 * @see FlinkCdc
 * @since 2021/3/16 11:47
 */

/*---------------------------------------------------------------------------------/
mysql
CREATE TABLE `source_table`  (
  `id` int(11) NOT NULL,
  `k` int(255) NULL DEFAULT NULL,
  `c` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pad` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remark` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

gp
CREATE TABLE sink_table  (
  id int8 NOT NULL,
  k int8 NULL DEFAULT NULL,
  c varchar(255)  COLLATE "pg_catalog"."default",
  pad varchar(255) COLLATE "pg_catalog"."default",
  PRIMARY KEY (id)
)

update srt
INSERT INTO sink_table(id, k, c, pad) VALUES (5, 5, '567', 'rrr') ON CONFLICT (id) DO UPDATE SET id=EXCLUDED.id, k=EXCLUDED.k, c=EXCLUDED.c, pad=EXCLUDED.pad
/---------------------------------------------------------------------------------*/
public class SQLExampleMysqlQuery {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);

        String sink = "\n" +
                "\n" +
                "create table a_aa(\n" +
                "`id` INT,\n" +
                "`name` STRING,\n" +
                "`age` INT,\n" +
                "`sex` STRING\n" +
                ")\n" +
                "WITH(\n" +
                "'connector'='jdbc',\n" +
                "'url'='jdbc:mysql://10.0.8.10:3306/anytxn_cf_transaction_dev8111',\n" +
                "'table-name'='aa',\n" +
                "'username'='any',\n" +
                "'password'='anywd1234')";

        String query = "select * from a_aa ";

        blinkStreamTableEnv.executeSql(sink);
        TableResult tableResult = blinkStreamTableEnv.executeSql(query);
        tableResult.print();

        blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
    }
}
