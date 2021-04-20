#关系数据库

##mysql

CREATE TABLE `flink_web`.`mysql_table`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tenant_id` char(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '租戶id',
  `account_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账户id',
  `organization_id` char(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构号',
  `account_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账户状态(0-新建 1-正常 8-关闭)',
  `product_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品id',
  `account_type` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账户类型(50-延滞账户)',
  `customer_id` char(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户id',
  `loan_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '	贷款订单id',
  `billing_tenor` smallint(6) NOT NULL COMMENT '第几期',
  `delq_days` smallint(6) NOT NULL COMMENT '当前延滞天数',
  `delq_date` date NOT NULL COMMENT '延滞日期',
  `last_delq_days` smallint(6) NOT NULL COMMENT '上一延滞天数',
  `total_amount_due` decimal(11, 2) NOT NULL COMMENT '应还款总金额(逾期总欠款 当期逾期本金+利息)',
  `delq_process_table_id` char(6) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '延滞处理参数id',
  `penalty_table_id` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '罚息参数表id',
  `joint_loan_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联合贷标识(0=否 1=是)',
  `chargeoff_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '核销标志(0-否 1-已核销)',
  `fund_id` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资金源id',
  `channel` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务渠道编号',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `update_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '最后更新操作员',
  `record_version_number` int(11) NOT NULL COMMENT '记录版本号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_accountid`(`account_id`) USING BTREE,
  INDEX `idx_loanid`(`loan_id`) USING BTREE,
  INDEX `idx_customerid`(`customer_id`) USING BTREE,
  INDEX `idx_accountstatus`(`account_status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '延滞信息表' ROW_FORMAT = Dynamic;


#GP数据库

CREATE TABLE "public"."gp_table" (
  "id" int8 NOT NULL,
  "tenant_id" varchar(4) COLLATE "pg_catalog"."default",
  "account_id" varchar(32) COLLATE "pg_catalog"."default",
  "organization_id" varchar(4) COLLATE "pg_catalog"."default",
  "account_status" varchar(1) COLLATE "pg_catalog"."default",
  "product_id" varchar(32) COLLATE "pg_catalog"."default",
  "account_type" varchar(2) COLLATE "pg_catalog"."default",
  "customer_id" varchar(16) COLLATE "pg_catalog"."default",
  "loan_id" varchar(32) COLLATE "pg_catalog"."default",
  "billing_tenor" int4,
  "delq_days" int4,
  "delq_date" date,
  "last_delq_days" int4,
  "total_amount_due" numeric(11,2),
  "delq_process_table_id" varchar(6) COLLATE "pg_catalog"."default",
  "penalty_table_id" varchar(6) COLLATE "pg_catalog"."default",
  "joint_loan_flag" varchar(1) COLLATE "pg_catalog"."default",
  "chargeoff_flag" varchar(1) COLLATE "pg_catalog"."default",
  "fund_id" varchar(6) COLLATE "pg_catalog"."default",
  "channel" varchar(32) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "update_by" varchar(20) COLLATE "pg_catalog"."default",
  "record_version_number" int4
)
;
