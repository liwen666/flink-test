
#172 yarn
http://172.16.101.12:8088/cluster
http://172.16.101.12:50070/dfshealth.html#tab-overview

/data/apps/flink-1.12.0/bin/flink run   -m yarn-cluster -ys 1 -p 1 -yjm 1048 -ytm 1024  -yD containerized.heap-cutoff-ratio=0.1 -yD taskmanager.memory.off-heap=true -yD taskmanager.memory.size=100m -yD heartbeat.timeout=18000000  -c jrx.data.hub.flink.data.SQLExampleData2PG  /data/apps/example/SQLExampleData2PG.jar 


#建表模板
CREATE TABLE [catalog_name.][db_name.]table_name
  (
    { <physical_column_definition> | <metadata_column_definition> | <computed_column_definition> }[ , ...n]
    [ <watermark_definition> ]
    [ <table_constraint> ][ , ...n]
  )
  [COMMENT table_comment]
  [PARTITIONED BY (partition_column_name1, partition_column_name2, ...)]
  WITH (key1=val1, key2=val2, ...)
  [ LIKE source_table [( <like_options> )] ]
   
<physical_column_definition>:
  column_name column_type [ <column_constraint> ] [COMMENT column_comment]
  
<column_constraint>:
  [CONSTRAINT constraint_name] PRIMARY KEY NOT ENFORCED

<table_constraint>:
  [CONSTRAINT constraint_name] PRIMARY KEY (column_name, ...) NOT ENFORCED

<metadata_column_definition>:
  column_name column_type METADATA [ FROM metadata_key ] [ VIRTUAL ]

<computed_column_definition>:
  column_name AS computed_column_expression [COMMENT column_comment]

<watermark_definition>:
  WATERMARK FOR rowtime_column_name AS watermark_strategy_expression

<source_table>:
  [catalog_name.][db_name.]table_name

<like_options>:
{
   { INCLUDING | EXCLUDING } { ALL | CONSTRAINTS | PARTITIONS }
 | { INCLUDING | EXCLUDING | OVERWRITING } { GENERATED | OPTIONS | WATERMARKS } 
}[, ...]

#问题解决
##1flink的taskmanager的内存调整


#设置job名称
val tabConf = tableEnv.getConfig
onf.setString("pipeline.name", Common.jobName)


#里面构建job名称TableEnvironmentImpl



#CDC

DebeziumChangeConsumer 114行对binlog解析

RowDataDebeziumDeserializeSchema 98 行

RowDataDebeziumDeserializeSchema  358行函数处理字段数据

#消费kafka的配置

 " 'connector' = 'kafka',\n" +
                " 'properties.bootstrap.servers' = '11.11.1.79:9092',\n" +
                " 'topic' = 'debezium_test',\n" +
                " 'format' = 'debezium-json',\n" +
                // 最早分区消费
//                " 'scan.startup.mode' = 'earliest-offset',\n" +
                // 最近分区消费
//                " 'scan.startup.mode' = 'latest-offset',\n" +
                // 指定偏移量消费
//                 " 'scan.startup.mode' = 'specific-offsets',\n" +
//                 " 'scan.startup.specific-offsets' = 'partition:0,offset:71',\n" +
                // 指定时间戳
                " 'scan.startup.mode' = 'timestamp',\n" +
                 " 'scan.startup.timestamp-millis' = '1616490682000',\n" +

//                " 'sink.buffer-flush.max-rows' = '1',\n" +
                " 'properties.group.id' = 'CDC_TEST')\n";
                
                
 flink的其他命令
 1.flink list
 
 flink list：列出flink的job列表。
 
 flink list -r/--runing :列出正在运行的job
 
 flink list -s/--scheduled :列出已调度完成的job
 
 1
 2
 3
 4
 5
 6
 2.flink cancel
 
 flink cancel [options] <job_id> : 取消正在运行的job id
 
 flink cancel -s/--withSavepoint <path> <job_id> ： 取消正在运行的job，并保存到相应的保存点
 
 1
 2
 3
 4
 3.flink stop：仅仅针对Streaming job
 
 flink stop [options] <job_id>
 
 flink stop <job_id>：停止对应的job
 
 1
 2
 3
 4
 4.flink modify
 
 flink modify <job_id> [options] 
 
 flink modify <job_id> -p/--parallelism p : 修改job的并行度
 1
 2
 3
 5.flink savepoint（重要）
 
 flink savepoint [options] <job_id> <target directory>
 
 eg:
 
 # 触发保存点
 flink savepoint <job_id> <hdfs://xxxx/xx/x> : 将flink的快照保存到hdfs目录
 
 # 使用yarn触发保存点
 flink savepoint <job_id> <target_directory> -yid <application_id>
 
 # 使用savepoint取消作业
 flink cancel -s <tar_directory> <job_id>
 
 
 
 # 从保存点恢复
 flink run -s <target_directoey> [:runArgs]
 
 # 如果复原的程序，对逻辑做了修改，比如删除了算子可以指定allowNonRestoredState参数复原。
 flink run -s <target_directory> -n/--allowNonRestoredState [:runArgs]
 ————————————————
 版权声明：本文为CSDN博主「张行之」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 原文链接：https://blog.csdn.net/qq_33689414/article/details/90671685