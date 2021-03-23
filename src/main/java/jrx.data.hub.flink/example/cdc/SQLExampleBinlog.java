package jrx.data.hub.flink.example.cdc;

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
 * @since 2021/3/16 11:47
 */

public class SQLExampleBinlog {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);

        String ddlSource = "CREATE TABLE `file_incr_txn_bm_acct_loan` (\n" +
                "        `txa_number` varchar(32) ,\n" +
                "        `txa_type` char(2) ,\n" +
                "        `txa_status` char(1) ,\n" +
                "        `txa_business_module` char(2) ,\n" +
                "        `txn_ORGANIZATION_ID` char(4) ,\n" +
                "        `txn_type` char(5) ,\n" +
                "        `txn_code` char(5) ,\n" +
                "        `txn_system_date` timestamp  ,\n" +
                "        `txn_effective_date` timestamp  ,\n" +
                "        `TXN_EFFECTIVE_AMNT` decimal(18,5) ,\n" +
                "        `txn_effective_currency` char(3) ,\n" +
                "        `txn_posting_date` date ,\n" +
                "        `txn_posting_time` timestamp  ,\n" +
                "        `txn_posting_amnt` decimal(18,5) ,\n" +
                "        `txn_posting_currency` char(3) ,\n" +
                "        `txn_settle_amnt` decimal(18,5) ,\n" +
                "        `txn_settle_currency` char(3) ,\n" +
                "        `txn_markup_percent` decimal(18,6) ,\n" +
                "        `txn_exchange_rate` decimal(18,9) ,\n" +
                "        `txn_plastic_number` varchar(19) ,\n" +
                "        `txn_card_number_post` varchar(19) ,\n" +
                "        `txn_lbs_info` varchar(255) ,\n" +
                "        `txn_country` varchar(3) ,\n" +
                "        `txn_region` varchar(2) ,\n" +
                "        `txn_channel` varchar(32) ,\n" +
                "        `txn_sub_channel_1` varchar(6) ,\n" +
                "        `txn_sub_channel_2` varchar(6) ,\n" +
                "        `txn_mechant_number` varchar(16) ,\n" +
                "        `txn_material_flag` char(1) ,\n" +
                "        `txn_pos_ind` char(1) ,\n" +
                "        `txn_auth_code` char(6) ,\n" +
                "        `txn_batch_number` char(6) ,\n" +
                "        `txn_voucher_number` char(6) ,\n" +
                "        `txn_reference_number` char(12) ,\n" +
                "        `txn_cust_number` varchar(32) ,\n" +
                "        `txn_cust_name` varchar(60) ,\n" +
                "        `txn_cust_group_id` varchar(6) ,\n" +
                "        `txn_intr_start_date` date ,\n" +
                "        `txn_statment_day_date` date ,\n" +
                "        `txn_waive_intr_flag` char(1) ,\n" +
                "        `txn_waive_latechg_flag` char(1) ,\n" +
                "        `txn_chargeoff_flag` char(1) ,\n" +
                "        `txn_chargeoff_amnt` decimal(18,5) ,\n" +
                "        `txn_chargeoff_date` date ,\n" +
                "        `txn_chargeoff_rsn_cd` char(4) ,\n" +
                "        `txn_orig_txa_number` varchar(32) ,\n" +
                "        `txn_create_date` timestamp  ,\n" +
                "        `txn_last_update_date` timestamp  ,\n" +
                "        `txn_last_update_operator` varchar(40) ,\n" +
                "        `txn_parent_txa_number` varchar(32) ,\n" +
                "        `TXN_STMT_REPAYMENT_TABLEID` varchar(6) ,\n" +
                "        `PRO_CD` varchar(6) ,\n" +
                "        `TXN_ORIG_TXA_TYPE` char(2) ,\n" +
                "        `TXN_PARENT_TXA_TYPE` char(2) ,\n" +
                "        `txn_session_number` varchar(32) ,\n" +
                "        `txn_connect_txa_number` varchar(32) ,\n" +
                "        `txn_connect_txa_type` char(2) ,\n" +
                "        `loan_plan_table_id` char(6) ,\n" +
                "        `bal_trans_flag` char(1) ,\n" +
                "        `compensatory_flag` char(1) ,\n" +
                "        `joint_loan_flag` char(1) ,\n" +
                "        `fus_id` varchar(4) ,\n" +
                "        `loan_prin_table_id` char(6) ,\n" +
                "        `txn_intr_table_id` char(6) ,\n" +
                "        `loan_fee_table_id` char(6) ,\n" +
                "        `loan_payoff_table_id` char(6) ,\n" +
                "        `txn_pen_intr_table_id` char(6) ,\n" +
                "        `txn_cs_pen_intr_table_id` char(6) ,\n" +
                "        `txn_cs_intr_table_id` char(6) ,\n" +
                "        `txn_cs_loan_fee_table_id` char(6) ,\n" +
                "        `txn_cs_loan_prepay_table_id` char(6) ,\n" +
                "        `txn_cs_loan_payoff_table_id` char(6) ,\n" +
                "        `loan_billing_cycle` char(2) ,\n" +
                "        `loan_payment_due_date` date ,\n" +
                "        `first_payment_due_date` date ,\n" +
                "        `last_payment_due_date` date ,\n" +
                "        `loan_tenor` int ,\n" +
                "        `loan_fee_wavie_from` int ,\n" +
                "        `loan_fee_wavie_to` int ,\n" +
                "        `loan_status` char(3) ,\n" +
                "        `loan_tenor_typ` char(4) ,\n" +
                "        `loan_acq_ref_nbr` char(32) ,\n" +
                "        `loan_operator_id` char(20) ,\n" +
                "        `loan_mechant_name` varchar(60) ,\n" +
                "        `loan_b018_merchant_typ` char(4) ,\n" +
                "        `loan_order_number` char(32) ,\n" +
                "        `loan_status_reason` varchar(256) ,\n" +
                "        `loan_cast_tenor` int ,\n" +
                "        `loan_payment_tenor` int ,\n" +
                "        `loan_close_payment_date` date ,\n" +
                "        `loan_curr_balance` decimal(18,2) ,\n" +
                "        `loan_contract_id` varchar(16) ,\n" +
                "        `loan_channel_id` varchar(16) ,\n" +
                "        `loan_total_balance` decimal(18,2) ,\n" +
                "        `loan_total_principal_balance` decimal(18,2) ,\n" +
                "        `loan_total_interest_balance` decimal(18,2) ,\n" +
                "        `loan_total_service_fee_balance` decimal(18,2) ,\n" +
                "        `discount_be_interest` decimal(18,2) ,\n" +
                "        `discount_interest` decimal(18,2) ,\n" +
                "        `loan_total_penalty_fee` decimal(18,2) ,\n" +
                "        `loan_total_penalty_exempt_amnt` decimal(18,2) ,\n" +
                "        `coupon_flow_id` char(16) ,\n" +
                "        `loan_payment_date_dd` int ,\n" +
                "        `loan_daily_rate` decimal(18,10) ,\n" +
                "        `fus_daily_rate` decimal(18,8) ,\n" +
                "        `converted_daily_rate` decimal(18,8) ,\n" +
                "        `DAILY_RATE_BEFORE` decimal(18,10) ,\n" +
                "        `loan_prepay_table_id` char(6) ,\n" +
                "        `loan_contract_number` varchar(60) ,\n" +
                "        `business_type` varchar(2) ,\n" +
                "        `staff_name_lj` varchar(256) ,\n" +
                "        `staff_id_type` varchar(50) ,\n" +
                "        `staff_id_number` varchar(20) ,\n" +
                "        `staff_account` varchar(20) ,\n" +
                "        `check_digit` varchar(200) ,\n" +
                "        `serial_num` varchar(64) ,\n" +
                "        `loan_use` varchar(4) ,\n" +
                "        `staff_cust_number` varchar(16) ,\n" +
                "        `product_code` varchar(32) ,\n" +
                "        `txn_late_charge_fee_table_id` char(6) ,\n" +
                "        `compensatory_days` int ,\n" +
                "        `txn_lending_time` timestamp  ,\n" +
                "        `total_over_due_tenor` int ,\n" +
                "        `total_cmps_tenor` int ,\n" +
                "        `invest` varchar(10) ,\n" +
                "        `reveal_mobel` char(1) ,\n" +
                "        `back_fee_rate` decimal(18,2) ,\n" +
                "        `last_update_business_date` date ,\n" +
                "        `hp_posting_amnt` decimal(18,2) ,\n" +
                "        `ot_posting_amnt` decimal(18,2) ,\n" +
                "        `fus_percentage` decimal(3,2) ,\n" +
                "        `hp_curr_balance` decimal(18,2) ,\n" +
                "        `ot_curr_balance` decimal(18,2) ,\n" +
                "        `continue_over_due_tenor` int ,\n" +
                "        `mark` varchar(255) ,\n" +
                "        `txn_partner_pen_intr_table_id` varchar(6) ,\n" +
                "        `last_update_time` timestamp  ,\n" +
                "        `create_time` timestamp  ,\n" +
                "        `loan_start_date` date ,\n" +
                "        `real_channel` varchar(23) ,\n" +
                "        `is_stock_order` char(1) ,\n" +
                "        `asset_status` char(1) ,\n" +
                "        `portfolio_code` varchar(32) ,\n" +
                "        `has_loop` varchar(2) ,\n" +
                "        `bank_inner_account` varchar(32) ,\n" +
                "        `product_xindai_code` varchar(64) ,\n" +
                "        `invest_code` varchar(32) ,\n" +
                "        `daily_total_prin_bal` decimal(18,2) ,\n" +
                "        `hp_daily_total_prin_bal` decimal(18,2)\n" +
                "        )  with(\n" +
                "        'connector'='filesystem',\n" +
                "        'path'='D:\\work\\any-data-hub-parent\\any-data-hub-component\\flink-engine\\src\\main\\resources\\increment.txt',\n" +
                "        'format'='maxwell-json'\n" +
                "        )";

        String sink = "CREATE TABLE `gp_ods_txn_bm_acct_loan` (\n" +
                "        `txa_number` varchar(32) ,\n" +
                "        `txa_type` char(2) ,\n" +
                "        `txa_status` char(1) ,\n" +
                "        `txa_business_module` char(2) ,\n" +
                "        `txn_ORGANIZATION_ID` char(4) ,\n" +
                "        `txn_type` char(5) ,\n" +
                "        `txn_code` char(5) ,\n" +
                "        `txn_system_date` timestamp  ,\n" +
                "        `txn_effective_date` timestamp  ,\n" +
                "        `TXN_EFFECTIVE_AMNT` decimal(18,5) ,\n" +
                "        `txn_effective_currency` char(3) ,\n" +
                "        `txn_posting_date` date ,\n" +
                "        `txn_posting_time` timestamp  ,\n" +
                "        `txn_posting_amnt` decimal(18,5) ,\n" +
                "        `txn_posting_currency` char(3) ,\n" +
                "        `txn_settle_amnt` decimal(18,5) ,\n" +
                "        `txn_settle_currency` char(3) ,\n" +
                "        `txn_markup_percent` decimal(18,6) ,\n" +
                "        `txn_exchange_rate` decimal(18,9) ,\n" +
                "        `txn_plastic_number` varchar(19) ,\n" +
                "        `txn_card_number_post` varchar(19) ,\n" +
                "        `txn_lbs_info` varchar(255) ,\n" +
                "        `txn_country` varchar(3) ,\n" +
                "        `txn_region` varchar(2) ,\n" +
                "        `txn_channel` varchar(32) ,\n" +
                "        `txn_sub_channel_1` varchar(6) ,\n" +
                "        `txn_sub_channel_2` varchar(6) ,\n" +
                "        `txn_mechant_number` varchar(16) ,\n" +
                "        `txn_material_flag` char(1) ,\n" +
                "        `txn_pos_ind` char(1) ,\n" +
                "        `txn_auth_code` char(6) ,\n" +
                "        `txn_batch_number` char(6) ,\n" +
                "        `txn_voucher_number` char(6) ,\n" +
                "        `txn_reference_number` char(12) ,\n" +
                "        `txn_cust_number` varchar(32) ,\n" +
                "        `txn_cust_name` varchar(60) ,\n" +
                "        `txn_cust_group_id` varchar(6) ,\n" +
                "        `txn_intr_start_date` date ,\n" +
                "        `txn_statment_day_date` date ,\n" +
                "        `txn_waive_intr_flag` char(1) ,\n" +
                "        `txn_waive_latechg_flag` char(1) ,\n" +
                "        `txn_chargeoff_flag` char(1) ,\n" +
                "        `txn_chargeoff_amnt` decimal(18,5) ,\n" +
                "        `txn_chargeoff_date` date ,\n" +
                "        `txn_chargeoff_rsn_cd` char(4) ,\n" +
                "        `txn_orig_txa_number` varchar(32) ,\n" +
                "        `txn_create_date` timestamp  ,\n" +
                "        `txn_last_update_date` timestamp  ,\n" +
                "        `txn_last_update_operator` varchar(40) ,\n" +
                "        `txn_parent_txa_number` varchar(32) ,\n" +
                "        `TXN_STMT_REPAYMENT_TABLEID` varchar(6) ,\n" +
                "        `PRO_CD` varchar(6) ,\n" +
                "        `TXN_ORIG_TXA_TYPE` char(2) ,\n" +
                "        `TXN_PARENT_TXA_TYPE` char(2) ,\n" +
                "        `txn_session_number` varchar(32) ,\n" +
                "        `txn_connect_txa_number` varchar(32) ,\n" +
                "        `txn_connect_txa_type` char(2) ,\n" +
                "        `loan_plan_table_id` char(6) ,\n" +
                "        `bal_trans_flag` char(1) ,\n" +
                "        `compensatory_flag` char(1) ,\n" +
                "        `joint_loan_flag` char(1) ,\n" +
                "        `fus_id` varchar(4) ,\n" +
                "        `loan_prin_table_id` char(6) ,\n" +
                "        `txn_intr_table_id` char(6) ,\n" +
                "        `loan_fee_table_id` char(6) ,\n" +
                "        `loan_payoff_table_id` char(6) ,\n" +
                "        `txn_pen_intr_table_id` char(6) ,\n" +
                "        `txn_cs_pen_intr_table_id` char(6) ,\n" +
                "        `txn_cs_intr_table_id` char(6) ,\n" +
                "        `txn_cs_loan_fee_table_id` char(6) ,\n" +
                "        `txn_cs_loan_prepay_table_id` char(6) ,\n" +
                "        `txn_cs_loan_payoff_table_id` char(6) ,\n" +
                "        `loan_billing_cycle` char(2) ,\n" +
                "        `loan_payment_due_date` date ,\n" +
                "        `first_payment_due_date` date ,\n" +
                "        `last_payment_due_date` date ,\n" +
                "        `loan_tenor` int ,\n" +
                "        `loan_fee_wavie_from` int ,\n" +
                "        `loan_fee_wavie_to` int ,\n" +
                "        `loan_status` char(3) ,\n" +
                "        `loan_tenor_typ` char(4) ,\n" +
                "        `loan_acq_ref_nbr` char(32) ,\n" +
                "        `loan_operator_id` char(20) ,\n" +
                "        `loan_mechant_name` varchar(60) ,\n" +
                "        `loan_b018_merchant_typ` char(4) ,\n" +
                "        `loan_order_number` char(32) ,\n" +
                "        `loan_status_reason` varchar(256) ,\n" +
                "        `loan_cast_tenor` int ,\n" +
                "        `loan_payment_tenor` int ,\n" +
                "        `loan_close_payment_date` date ,\n" +
                "        `loan_curr_balance` decimal(18,2) ,\n" +
                "        `loan_contract_id` varchar(16) ,\n" +
                "        `loan_channel_id` varchar(16) ,\n" +
                "        `loan_total_balance` decimal(18,2) ,\n" +
                "        `loan_total_principal_balance` decimal(18,2) ,\n" +
                "        `loan_total_interest_balance` decimal(18,2) ,\n" +
                "        `loan_total_service_fee_balance` decimal(18,2) ,\n" +
                "        `discount_be_interest` decimal(18,2) ,\n" +
                "        `discount_interest` decimal(18,2) ,\n" +
                "        `loan_total_penalty_fee` decimal(18,2) ,\n" +
                "        `loan_total_penalty_exempt_amnt` decimal(18,2) ,\n" +
                "        `coupon_flow_id` char(16) ,\n" +
                "        `loan_payment_date_dd` int ,\n" +
                "        `loan_daily_rate` decimal(18,10) ,\n" +
                "        `fus_daily_rate` decimal(18,8) ,\n" +
                "        `converted_daily_rate` decimal(18,8) ,\n" +
                "        `DAILY_RATE_BEFORE` decimal(18,10) ,\n" +
                "        `loan_prepay_table_id` char(6) ,\n" +
                "        `loan_contract_number` varchar(60) ,\n" +
                "        `business_type` varchar(2) ,\n" +
                "        `staff_name_lj` varchar(256) ,\n" +
                "        `staff_id_type` varchar(50) ,\n" +
                "        `staff_id_number` varchar(20) ,\n" +
                "        `staff_account` varchar(20) ,\n" +
                "        `check_digit` varchar(200) ,\n" +
                "        `serial_num` varchar(64) ,\n" +
                "        `loan_use` varchar(4) ,\n" +
                "        `staff_cust_number` varchar(16) ,\n" +
                "        `product_code` varchar(32) ,\n" +
                "        `txn_late_charge_fee_table_id` char(6) ,\n" +
                "        `compensatory_days` int ,\n" +
                "        `txn_lending_time` timestamp  ,\n" +
                "        `total_over_due_tenor` int ,\n" +
                "        `total_cmps_tenor` int ,\n" +
                "        `invest` varchar(10) ,\n" +
                "        `reveal_mobel` char(1) ,\n" +
                "        `back_fee_rate` decimal(18,2) ,\n" +
                "        `last_update_business_date` date ,\n" +
                "        `hp_posting_amnt` decimal(18,2) ,\n" +
                "        `ot_posting_amnt` decimal(18,2) ,\n" +
                "        `fus_percentage` decimal(3,2) ,\n" +
                "        `hp_curr_balance` decimal(18,2) ,\n" +
                "        `ot_curr_balance` decimal(18,2) ,\n" +
                "        `continue_over_due_tenor` int ,\n" +
                "        `mark` varchar(255) ,\n" +
                "        `txn_partner_pen_intr_table_id` varchar(6) ,\n" +
                "        `last_update_time` timestamp  ,\n" +
                "        `create_time` timestamp  ,\n" +
                "        `loan_start_date` date ,\n" +
                "        `real_channel` varchar(23) ,\n" +
                "        `is_stock_order` char(1) ,\n" +
                "        `asset_status` char(1) ,\n" +
                "        `portfolio_code` varchar(32) ,\n" +
                "        `has_loop` varchar(2) ,\n" +
                "        `bank_inner_account` varchar(32) ,\n" +
                "        `product_xindai_code` varchar(64) ,\n" +
                "        `invest_code` varchar(32) ,\n" +
                "        `daily_total_prin_bal` decimal(18,2) ,\n" +
                "        `hp_daily_total_prin_bal` decimal(18,2) ,\n" +
                "         PRIMARY KEY (`txa_number`) NOT ENFORCED\n" +
                "        )  with (\n" +
                " 'connector' = 'jdbc',\n" +
                " 'url' = 'jdbc:mysql://11.11.1.79/mydb?useSSL=false&useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC',\n" +
                " 'username' = 'root',\n" +
                " 'password' = 'root',\n" +
                " 'table-name' = 'bm_acct_loan',\n" +
                " 'driver' = 'com.mysql.cj.jdbc.Driver',\n" +
                " 'sink.buffer-flush.interval' = '3s',\n" +
                " 'sink.buffer-flush.max-rows' = '1',\n" +
                " 'sink.max-retries' = '5')\n";
        String job = " INSERT INTO gp_ods_txn_bm_acct_loan SELECT * FROM file_incr_txn_bm_acct_loan\n";

        blinkStreamTableEnv.executeSql(ddlSource);
        blinkStreamTableEnv.executeSql(sink);
        TableResult tableResult = blinkStreamTableEnv.executeSql(job);

        blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
    }
}
