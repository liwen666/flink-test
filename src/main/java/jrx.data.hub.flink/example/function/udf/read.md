#mysql 表
CREATE TABLE `cdc_test` (
  `id` int(11) NOT NULL,
  `k` int(255) DEFAULT NULL,
  `c` varchar(255) DEFAULT NULL,
  `pad` varchar(255) DEFAULT NULL,
  `remark` varchar(30) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

#GP 表
CREATE TABLE function_test  (
  id int4,
  k int8 ,
  c varchar(255) COLLATE "pg_catalog"."default",
  pad varchar(255) COLLATE "pg_catalog"."default",
  remark varchar(30) ,
  create_time timestamp(0) ,
  update_time timestamp(6) NULL DEFAULT NULL,
  PRIMARY KEY (id) 
) 




create table mysql_table(
`id` INT,
`k` INT,
`c` STRING,
`pad` STRING,
`remark` STRING,
`create_time` TIMESTAMP(3),
`update_time` TIMESTAMP
)
WITH(
'connector'='mysql-cdc',
'hostname'='11.11.1.79',
'port'='3306',
'password'='root',
'username'='root',
'database-name'='flink_web',
'table-name'='cdc_test');
create table gp_table(
`create_time` TIMESTAMP,
`update_time` TIMESTAMP,
`c` STRING,
`pad` STRING,
`remark` STRING,
`id` INT,
`k` BIGINT
)
WITH(
'connector'='greenplum',
'schema-name'='public',
'url'='jdbc:postgresql://10.0.8.10:5432/flink_sync',
'table-name'='function_test',
'username'='gpadmin',
'password'='gpadmin');


