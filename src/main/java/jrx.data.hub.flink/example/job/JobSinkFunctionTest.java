package jrx.data.hub.flink.example.job;

import jrx.data.hub.flink.example.cdc.SQLExampleCdcKafka;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * <p>
 *  描述
 * </p>
 *
 * @author lw
 * @since  2021/3/16 11:47
 * @see SQLExampleCdcKafka
 */

public class JobSinkFunctionTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);
        String source = "create table kods_am_delinquency_info_testy(\n" +
                "`id` BIGINT,\n" +
                "`tenant_id` STRING,\n" +
                "`account_id` STRING,\n" +
                "`organization_id` STRING,\n" +
                "`product_id` STRING,\n" +
                "`account_status` STRING,\n" +
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
                "`create_time` TIMESTAMP ,\n" +
                "`chargeoff_flag` STRING,\n" +
                "`fund_id` STRING,\n" +
                "`channel` STRING,\n" +
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
                "'format'='debezium-json')";

        String sink = "create table gp_ods_am_delinquency_info_bak(\n" +
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
                "`channel` STRING,\n" +
                "`create_time` TIMESTAMP,\n" +
                "`fund_id` STRING,\n" +
                "`update_time` TIMESTAMP,\n" +
                "`update_by` STRING,\n" +
                "`record_version_number` INT\n" +
                ")\n" +
                "WITH(\n" +
                "'connector'='greenplum',\n" +
                "'schema-name'='public',\n" +
                "'url'='jdbc:postgresql://10.0.8.10:5432/real_time_dataware',\n" +
                "'table-name'='ods_am_delinquency_info_bak',\n" +
                "'username'='gpadmin',\n" +
                "'password'='gpadmin')";
        String job = " INSERT INTO gp_ods_am_delinquency_info_bak\n ( id , \n tenant_id , \n account_id , \n organization_id , \n account_status , \n product_id , \n account_type , \n customer_id , \n loan_id , \n billing_tenor , \n delq_days , \n delq_date , \n last_delq_days , \n total_amount_due , \n delq_process_table_id , \n penalty_table_id , \n joint_loan_flag , \n chargeoff_flag , \n channel , \n create_time , \n fund_id , \n update_time , \n update_by , \n record_version_number )\n SELECT \n id , \n tenant_id , \n account_id , \n organization_id , \n account_status , \n product_id , \n account_type , \n customer_id , \n loan_id , \n billing_tenor , \n delq_days , \n delq_date , \n last_delq_days , \n total_amount_due , \n delq_process_table_id , \n penalty_table_id , \n joint_loan_flag , \n chargeoff_flag , \n channel , \n create_time , \n fund_id , \n update_time , \n update_by , \n record_version_number \n FROM kods_am_delinquency_info_testy";

        blinkStreamTableEnv.executeSql(source);
        blinkStreamTableEnv.executeSql(sink);
        TableResult tableResult = blinkStreamTableEnv.executeSql(job);

        blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
    }
}
