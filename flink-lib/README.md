20201206  TODO
1.新建kafka数据源页面,缺少kafka topic级参数
2.采集过的ddl语句保存入库,comment单独存一个字段
3.数据源列表分为采集和自定义两种类型



#字段表存取问题
字段的更新维护
字段的修改与删除（暂时不考虑）


#IDEA 自动化部署插件
jrebel  
guid成器
http://www.cicoding.cn/other/jrebel-activation/
生成guid**
http://jrebel.cicoding.cn/guid
http://jrebel.cicoding.cn/78B9F0BF-7C84-F0B9-ED8C-E8402A67C2DB

javadoc

     Locale  zh_CN
    -encoding UTF-8 -charset UTF-8 -windowtitle "你的文档在浏览器窗口标题栏显示的内容" -link http://docs.Oracle.com/javase/7/docs/api




#数据同步测试
mysql->gp
##mysql 
CREATE TABLE `flink_job_config` (
  `query_id` char(40) NOT NULL COMMENT 'sql语句流水id',
  `resource_id` char(255) DEFAULT NULL COMMENT 'jobID',
  `sql_query` char(255) NOT NULL COMMENT '查询sql语句',
  `flink_tables` longtext COMMENT '查询sql关联表',
  `udf_fun` longtext COMMENT '用户自定义函数',
  `udaf_fun` longtext COMMENT '用户自定义计算函数',
  `udtf_fun` longtext COMMENT '用户自定义表函数',
  `function_path` varchar(255) DEFAULT NULL COMMENT '依赖路径地址',
  `content_code` varchar(50) DEFAULT NULL COMMENT '租户号',
  `create_person` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_person` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`query_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='flink应用程序表'

##gp表

CREATE TABLE "public"."flink_job_config" (
  "query_id" char(40) COLLATE "pg_catalog"."default" NOT NULL,
  "resource_id" varchar(100) COLLATE "pg_catalog"."default",
  "sql_query" char(255) COLLATE "pg_catalog"."default",
  "flink_tables" text COLLATE "pg_catalog"."default",
  "udf_fun" text COLLATE "pg_catalog"."default",
  "udaf_fun" text COLLATE "pg_catalog"."default",
  "udtf_fun" text COLLATE "pg_catalog"."default",
  "function_path" varchar(255) COLLATE "pg_catalog"."default",
  "content_code" varchar(50) COLLATE "pg_catalog"."default",
  "create_person" varchar(50) COLLATE "pg_catalog"."default",
  "update_person" varchar(50) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  PRIMARY KEY ("query_id")
)
;

COMMENT ON COLUMN "public"."flink_job_config"."query_id" IS '主键';



