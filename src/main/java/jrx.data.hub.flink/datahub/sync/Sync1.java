package jrx.data.hub.flink.datahub.sync;

import jrx.data.hub.flink.example.cdc.multiple.FlinkCdc;
import org.apache.flink.api.common.JobID;
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
public class Sync1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);

        String t1 = "CREATE TABLE `gp_ods_txn_am_interest_accrual_info` (\n" +
                "        `id` BIGINT ,\n" +
                "        `tenant_id` varchar(4) ,\n" +
                "        `account_id` varchar(32) ,\n" +
                "        `organization_id` varchar(4) ,\n" +
                "        `channel` varchar(32) ,\n" +
                "        `account_status` varchar(1) ,\n" +
                "        `product_id` varchar(32) ,\n" +
                "        `account_type` varchar(2) ,\n" +
                "        `customer_id` varchar(16) ,\n" +
                "        `loan_id` varchar(32) ,\n" +
                "        `billing_tenor` INT ,\n" +
                "        `accrual_base_amount` numeric(18,2),\n" +
                "        `accrual_daily_rate` numeric(18,10),\n" +
                "        `accrual_start_date` date ,\n" +
                "        `accrual_total_accru_interest` numeric(18,2),\n" +
                "        `accrual_total_accru_days` INT ,\n" +
                "        `accrual_new_interest` numeric(18,10),\n" +
                "        `accrual_new_days` INT,\n" +
                "        `accrual_new_from_date` date,\n" +
                "        `accrual_new_to_date` date,\n" +
                "        `accrual_daily_adjustment` numeric(18,10),\n" +
                "        `accrual_daily_after_adjustment` numeric(18,10),\n" +
                "        `accrual_daily_provision` numeric(18,2),\n" +
                "        `stop_accrual_flag` varchar(1) ,\n" +
                "        `max_interest` numeric(18,2),\n" +
                "        `interest_table_id` varchar(6) ,\n" +
                "        `penalty_table_id` varchar(6) ,\n" +
                "        `joint_loan_flag` varchar(1) ,\n" +
                "        `payment_due_date` date ,\n" +
                "        `profit_shared` varchar(1) ,\n" +
                "        `bal_transfer_flag` varchar(1) ,\n" +
                "        `compensatory_flag` varchar(1) ,\n" +
                "        `total_tenor` INT ,\n" +
                "        `total_days` INT ,\n" +
                "        `accounting_phase` varchar(1) ,\n" +
                "        `non_accruals_flag` varchar(1) ,\n" +
                "        `loan_classify` varchar(1) ,\n" +
                "        `tax_amt` numeric(18,2),\n" +
                "        `liability_flag` varchar(2) ,\n" +
                "        `create_time` timestamp(6) ,\n" +
                "        `update_time` timestamp(6) ,\n" +
                "        `update_by` varchar(20) ,\n" +
                "        `record_version_number` INT ,\n" +
                "        `txn_code` varchar(6)\n" +
                "        )\n" +
                "WITH(\n" +
                "'connector'='greenplum',\n" +
                "'schema-name'='public',\n" +
                "'url'='jdbc:postgresql://10.0.8.10:5432/anyest3_financial_cloud_new_101',\n" +
                "'table-name'='ods_txn_am_interest_accrual_info',\n" +
                "'username'='gpadmin',\n" +
                "'password'='gpadmin')";

        String t2 ="CREATE TABLE `kafka_txn_am_interest_accrual_info` (\n" +
                "            `id` BIGINT ,\n" +
                "            `tenant_id` STRING ,\n" +
                "            `account_id` STRING ,\n" +
                "            `organization_id` STRING ,\n" +
                "            `channel` STRING ,\n" +
                "            `account_status` STRING ,\n" +
                "            `product_id` STRING ,\n" +
                "            `account_type` STRING ,\n" +
                "            `customer_id` STRING ,\n" +
                "            `loan_id` STRING ,\n" +
                "            `billing_tenor` INT ,\n" +
                "            `accrual_base_amount` DECIMAL(18,2),\n" +
                "            `accrual_daily_rate` DECIMAL(18,10),\n" +
                "            `accrual_start_date` INT ,\n" +
                "            `accrual_total_accru_interest` DECIMAL(18,2),\n" +
                "            `accrual_total_accru_days` INT ,\n" +
                "            `accrual_new_interest` DECIMAL(18,10),\n" +
                "            `accrual_new_days` INT,\n" +
                "            `accrual_new_from_date` INT,\n" +
                "            `accrual_new_to_date` INT,\n" +
                "            `accrual_daily_adjustment` DECIMAL(18,10),\n" +
                "            `accrual_daily_after_adjustment` DECIMAL(18,10),\n" +
                "            `accrual_daily_provision` DECIMAL(18,2),\n" +
                "            `stop_accrual_flag` STRING ,\n" +
                "            `max_interest` DECIMAL(18,2),\n" +
                "            `interest_table_id` STRING ,\n" +
                "            `penalty_table_id` STRING ,\n" +
                "            `joint_loan_flag` STRING ,\n" +
                "            `payment_due_date` INT ,\n" +
                "            `profit_shared` STRING ,\n" +
                "            `bal_transfer_flag` STRING ,\n" +
                "            `compensatory_flag` STRING ,\n" +
                "            `total_tenor` INT ,\n" +
                "            `total_days` INT ,\n" +
                "            `accounting_phase` STRING ,\n" +
                "            `non_accruals_flag` STRING ,\n" +
                "            `loan_classify` STRING ,\n" +
                "            `tax_amt` DECIMAL(18,2),\n" +
                "            `liability_flag` STRING ,\n" +
                "            `create_time` STRING ,\n" +
                "            `update_time` STRING ,\n" +
                "            `update_by` STRING ,\n" +
                "            `record_version_number` INT ,\n" +
                "            `txn_code` STRING,\n" +
                "            `binlog_ts` TIMESTAMP(3) METADATA FROM 'value.source.timestamp'\n" +
                "        ) \n" +
                "WITH(\n" +
                "'connector'='kafka',\n" +
                "'properties.bootstrap.servers'='192.168.56.101:9092',\n" +
                "'properties.group.id'='testGroup',\n" +
                "'format'='debezium-json')";



        blinkStreamTableEnv.executeSql(t1);
        blinkStreamTableEnv.executeSql(t2);
        TableResult tableResult1 = blinkStreamTableEnv.executeSql("INSERT INTO gp_ods_txn_am_interest_accrual_info\n        SELECT\n            id ,\n            tenant_id ,\n            account_id ,\n            organization_id ,\n            channel ,\n            account_status ,\n            product_id ,\n            account_type ,\n            customer_id ,\n            loan_id ,\n            billing_tenor ,\n            accrual_base_amount ,\n            accrual_daily_rate ,\n            return_date(accrual_start_date) ,\n            accrual_total_accru_interest ,\n            accrual_total_accru_days ,\n            accrual_new_interest ,\n            accrual_new_days ,\n            return_date(accrual_new_from_date) ,\n            return_date(accrual_new_to_date) ,\n            accrual_daily_adjustment ,\n            accrual_daily_after_adjustment ,\n            accrual_daily_provision ,\n            stop_accrual_flag ,\n            max_interest ,\n            interest_table_id ,\n            penalty_table_id ,\n            joint_loan_flag ,\n            return_date(payment_due_date) ,\n            profit_shared ,\n            bal_transfer_flag ,\n            compensatory_flag ,\n            total_tenor ,\n            total_days ,\n            accounting_phase ,\n            non_accruals_flag ,\n            loan_classify ,\n            tax_amt ,\n            liability_flag ,\n            return_date(create_time) ,\n            return_date(update_time) ,\n            update_by ,\n            record_version_number ,\n            txn_code\n        FROM kafka_txn_am_interest_accrual_info");
        JobID jobID = tableResult1.getJobClient().get().getJobID();
        System.out.println(jobID);
//        TableResult tableResult = blinkStreamTableEnv.executeSql(query);
//        tableResult.print();
//        TableResult tableResult2 = blinkStreamTableEnv.executeSql(query2);
//        tableResult2.print();

        blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
    }
}
