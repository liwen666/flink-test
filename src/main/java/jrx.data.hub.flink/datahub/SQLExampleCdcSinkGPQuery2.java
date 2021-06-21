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
public class SQLExampleCdcSinkGPQuery2 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);

        String t1 = "\n" +
                "\n" +
                " CREATE TABLE `k_ods_task_tsk_pad_customer_marketing` (\n" +
                "             id int,\n" +
                "             claim_status STRING,\n" +
                "             claim_update_time STRING,\n" +
                "             accept_user_id STRING ,\n" +
                "             accept_user_name STRING ,\n" +
                "             cust_type STRING ,\n" +
                "             marketing_status STRING ,\n" +
                "             point_change_status STRING ,\n" +
                "             cust_name STRING ,\n" +
                "             mobile_no STRING ,\n" +
                "             cust_id_no STRING ,\n" +
                "             cust_company_name STRING ,\n" +
                "             cust_company_type STRING ,\n" +
                "             sea_num STRING ,\n" +
                "             create_white_list_time int,\n" +
                "             organization STRING ,\n" +
                "             customer_source STRING ,\n" +
                "             tenant_id STRING ,\n" +
                "             create_time bigint,\n" +
                "             update_time bigint\n" +
                "        )\n" +
                "WITH(\n" +
                "'connector'='kafka',\n" +
                "'topic'='cdyh101.ods_task_tsk_pad_customer_marketing',\n" +
                "'properties.bootstrap.servers'='39.0.158.101:9092',\n" +
                "'properties.group.id'='highsea',\n" +
                "'format'='debezium-json',\n" +
                "'scan.startup.mode'='earliest-offset')";


        String q1 = "select * from k_ods_task_tsk_pad_customer_marketing ";

        blinkStreamTableEnv.executeSql(t1);
        TableResult tableResult1 = blinkStreamTableEnv.executeSql(q1);
        tableResult1.print();
//        TableResult tableResult = blinkStreamTableEnv.executeSql(query);
//        tableResult.print();
//        TableResult tableResult2 = blinkStreamTableEnv.executeSql(query2);
//        tableResult2.print();

        blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
    }
}
