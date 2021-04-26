        CREATE TABLE kafka_binlog_dml(
            binlog string
        )
WITH(
'connector'='kafka',
'topic'='tayh811.am_account_main_info;tayh811.am_delinquency_info;tayh811.am_interest_accrual_info;tayh811.am_loan_account_info;tayh811.cm_customer_second_level;tayh811.pr_amort_method;tayh811.tl_gl_interface_info;tayh811.cm_customer_limit_info;tayh811.tl_payment_allocation_log',
'properties.bootstrap.servers'='172.16.102.23:9092',
'properties.group.id'='ODS_T_Group',
'value.format'='raw',
'scan.startup.mode'='earliest-offset')|||
 CREATE TABLE file_binlog_dml(
            binlog string,
            tb string,
            biz_date string
        )
PARTITIONED BY (tb,biz_date)
WITH(
'connector'='filesystem',
'path'='file:///D:\flink12\flink-1.12.1-src\flink-engine\src\main\java\jrx.data.hub.flink\example\file\test',
'sink.rolling-policy.file-size'='128MB',
'sink.rolling-policy.rollover-interval'='1 min',
'sink.rolling-policy.check-interval'='1 min',
'format'='raw',
'sink.partition-commit.policy.kind'='success-file,custom',
'sink.partition-commit.policy.class'='jrx.data.hub.flink.example.file.MergeComitPolicy',
'sink.partition-commit.success-file.name'='_SUCCESS')|||