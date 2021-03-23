
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
接受debezium消息时不能使用debezium开头的topic