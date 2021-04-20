package jrx.data.hub.flink.poc.kafkatogp;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * Desc: sink in PG
 * Created by zhisheng on 2020-03-19 08:36
 */
public class KafkaToGp {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);

        String ddlSource = "create table kods_am_delinquency_info_testy(\n" +
                "`id` BIGINT,\n" +
                "`tenant_id` STRING,\n" +
                "`account_id` STRING,\n" +
                "`organization_id` STRING,\n" +
                "`account_status` STRING,\n" +
                "`product_id` STRING,\n" +
                "`account_type` STRING,\n" +
                "`customer_id` STRING,\n" +
                "`loan_id` STRING,\n" +
                "`billing_tenor` SMALLINT,\n" +
                "`delq_days` SMALLINT,\n" +
                "`delq_date` DATE,\n" +
                "`last_delq_days` SMALLINT,\n" +
                "`total_amount_due` DECIMAL(18,10),\n" +
                "`delq_process_table_id` STRING,\n" +
                "`penalty_table_id` STRING,\n" +
                "`joint_loan_flag` STRING,\n" +
                "`chargeoff_flag` STRING,\n" +
                "`fund_id` STRING,\n" +
                "`channel` STRING,\n" +
                "`create_time` TIMESTAMP,\n" +
                "`update_time` TIMESTAMP,\n" +
                "`update_by` STRING,\n" +
                "`record_version_number` INT,\n" +
                "`binlog_ts` TIMESTAMP(3)\n" +
                ")\n" +
                "WITH(\n" +
                "'connector'='kafka',\n" +
                "'topic'='kods_am_delinquency_info_testy',\n" +
                "'properties.bootstrap.servers'='172.16.102.23:9092',\n" +
                "'properties.group.id'='ods_group_id',\n" +
                " 'scan.startup.mode' = 'earliest-offset',\n" +
                "'format'='debezium-json')";



        String ddlSink = "create table gp_ods_am_delinquency_info_bak(\n" +
                "`total_amount_due` DECIMAL(18,10),\n" +
                "`create_time` TIMESTAMP,\n" +
                "`update_time` TIMESTAMP,\n" +
                "`delq_date` DATE,\n" +
                "`tenant_id` STRING,\n" +
                "`account_id` STRING,\n" +
                "`organization_id` STRING,\n" +
                "`account_status` STRING,\n" +
                "`product_id` STRING,\n" +
                "`account_type` STRING,\n" +
                "`customer_id` STRING,\n" +
                "`loan_id` STRING,\n" +
                "`delq_process_table_id` STRING,\n" +
                "`joint_loan_flag` STRING,\n" +
                "`update_by` STRING,\n" +
                "`penalty_table_id` STRING,\n" +
                "`chargeoff_flag` STRING,\n" +
                "`fund_id` STRING,\n" +
                "`channel` STRING,\n" +
                "`record_version_number` INT,\n" +
                "`billing_tenor` SMALLINT,\n" +
                "`delq_days` SMALLINT,\n" +
                "`last_delq_days` SMALLINT,\n" +
                "`id` BIGINT\n" +
                ")\n" +
                "WITH(\n" +
                "        'connector' = 'greenplum',\n" +
                "        'sink.buffer-flush.max-rows' = '500',\n" +
                "        'sink.buffer-flush.interval' = '15000',\n" +
                "        'schema-name' = 'public',\n" +
                "'url'='jdbc:postgresql://10.0.8.10:5432/real_time_dataware',\n" +
                "'table-name'='ods_txn_am_delinquency_info_bak',\n" +
                "'username'='gpadmin',\n" +
                "'password'='gpadmin')";

        String sql = "insert into gp_ods_am_delinquency_info_bak\nselect    total_amount_due,\n        create_time ,\n        update_time,\n        delq_date ,\n        tenant_id ,\n        account_id ,\n        organization_id ,\n        account_status ,\n        product_id ,\n        account_type ,\n        customer_id ,\n        loan_id ,\n        delq_process_table_id ,\n        joint_loan_flag ,\n        update_by ,\n        penalty_table_id ,\n        chargeoff_flag ,\n        fund_id ,\n        channel ,\n        record_version_number,\n        billing_tenor ,\n        delq_days ,\n        last_delq_days ,\n        id\n    FROM kods_am_delinquency_info_testy";


        blinkStreamTableEnv.executeSql(ddlSource);
        blinkStreamTableEnv.executeSql(ddlSink);
        blinkStreamTableEnv.executeSql(sql);



        try {
            blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
