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
public class SQLExampleCdcSinkGPQuery1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);

        String t1 = "\t   \n" +
                "\t   create table gp_dim_cust_whitelist_his(\n" +
                "`cust_type` STRING,\n" +
                "`custname` STRING,\n" +
                "`type_code` STRING,\n" +
                "`rec_manager_id` STRING,\n" +
                "`branch_name` STRING,\n" +
                "`job` STRING,\n" +
                "`icnumber` STRING,\n" +
                "`mobile` STRING,\n" +
                "`companyname` STRING,\n" +
                "`price_tag` STRING,\n" +
                "`amt_tag` STRING,\n" +
                "`education_type` STRING,\n" +
                "`sms_tag` STRING,\n" +
                "`repayamt` DOUBLE,\n" +
                "`ave3msalary` DOUBLE,\n" +
                "`price_list` DOUBLE,\n" +
                "`amt_list` DOUBLE,\n" +
                "`bal_finance_3mon` DOUBLE,\n" +
                "`avg_deposite_bal_3mon` DOUBLE,\n" +
                "`bal_property_on_loan_cal` DOUBLE,\n" +
                "`mortgage_remn_periods_cal` INT,\n" +
                "`on_loan_property_amt_cal` INT,\n" +
                "`rank` INT,\n" +
//                "`branch_code` INT,\n" +
                "`bal_property_on_loan` INT,\n" +
                "`on_loan_property_amt` INT,\n" +
                "`mortgage_remn_periods` INT,\n" +
                "`worker_type` INT,\n" +
                "`annual_pay` INT,\n" +
                "`contn_salary_3mon` INT,\n" +
                "`worker_year` INT,\n" +
                "`biz_date` INT\n" +
                ")\n" +
                "\t" +
                "WITH(\n" +
                "'connector'='greenplum',\n" +
                "'schema-name'='public',\n" +
                "'url'='jdbc:postgresql://172.16.103.105:5432/anyest3_financial_cloud_101',\n" +
                "'table-name'='dim_cust_whitelist_his',\n" +
                "'username'='gpadmin',\n" +
                "'password'='gpadmin',\n" +
                "'sink.buffer-flush.max-rows'='5000',\n" +
                "'sink.buffer-flush.interval'='15000')";


        String q1 = "select * from gp_dim_cust_whitelist_his ";

        blinkStreamTableEnv.executeSql(t1);
        TableResult tableResult1 = blinkStreamTableEnv.executeSql(q1);
        System.out.println(tableResult1.getJobClient().get().getJobID().toString());
        tableResult1.print();
//        TableResult tableResult = blinkStreamTableEnv.executeSql(query);
//        tableResult.print();
//        TableResult tableResult2 = blinkStreamTableEnv.executeSql(query2);
//        tableResult2.print();

        blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
    }
}
