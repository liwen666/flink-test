
#单机
/home/liwen/flink12/bin/flink run -m 11.11.1.79:8081  -c jrx.data.hub.flink.example.scoket.SocketWindowWordCount  /home/liwen/flink12/examples/SocketWindowWordCount.jar 


/home/liwen/flink12/bin/flink run -m 11.11.1.79:8081  -c org.apache.flink.examples.java.wordcount.WordCount  /home/liwen/flink12/examples/batch/WordCount.jar 
 /home/liwen/flink12/bin/flink run    -p 1   -c jrx.data.hub.flink.example.table.MysqlComplemented  /home/liwen/flink12/examples/MysqlComplemented.jar   --script /home/liwen/flink12/examples

 /home/liwen/flink12/bin/flink run -m 11.11.1.79:8081    -p 1   -c jrx.data.hub.flink.example.table.MysqlComplemented  /home/liwen/flink12/examples/MysqlComplemented.jar   --script /home/liwen/flink12/examples
 
 /home/liwen/application/flink/flink-server/bin/flink run -m 10.0.8.13:8081  -c org.apache.flink.examples.java.wordcount.WordCount  /home/liwen/application/flink/flink-server/examples/batch/WordCount.jar 
/home/liwen/flink12/bin/flink run -m 10.0.8.13:8081  -c org.apache.flink.examples.java.wordcount.WordCount  /home/liwen/flink12/examples/batch/WordCount.jar 

/home/liwen/flink12/flink12/bin/flink run -m 10.0.8.13:8081  -c org.apache.flink.examples.java.wordcount.WordCount  /home/liwen/flink12/flink12/examples/batch/WordCount.jar 

#本地测试函数
 /home/liwen/flink12/bin/flink run    -p 1   -c jrx.data.hub.flink.example.table.SQLExampleData2PGFunction  /home/liwen/flink12/examples/SQLExampleData2PGFunction.jar

 /home/liwen/flink12/bin/flink run    -p 1   -c jrx.data.hub.flink.example.table.SQLExampleData2PGFunction  /home/liwen/flink12/examples/function/SQLExampleData2PGFunction.jar


#测试环境

/data/apps/flink-1.11.2/bin/flink run -m 10.0.8.13:8081  -c org.apache.flink.examples.java.wordcount.WordCount  /data/apps/flink-1.11.2/examples/batch/WordCount.jar 


/data/apps/flink-1.11.2/bin/flink run -m 10.0.8.13:8081   -c jrx.data.hub.flink.example.table.MysqlComplemented  /data/apps/flink-1.11.2/examples/MysqlComplemented.jar   --script /data/apps/flink-1.11.2/examples



#yarn模式
/home/liwen/flink12/flink12/bin/flink run   -m yarn-cluster   -c org.apache.flink.examples.java.wordcount.WordCount  /home/liwen/flink12/flink12/examples/batch/WordCount.jar 
/home/liwen/flink12/flink12/bin/flink run   -m yarn-cluster   -c org.apache.flink.examples.java.wordcount.WordCount  /home/liwen/flink12/flink12/examples/batch/WordCount.jar  --output hdfs:///flink/out/abc.txt


#11.11.1.79实验
flink安装目录  /home/liwen/flink12
/home/liwen/flink12/bin/flink run -m 11.11.1.79:8081    -c org.apache.flink.examples.java.wordcount.WordCount  /home/liwen/flink12/examples/batch/WordCount.jar  


/home/liwen/flink12/bin/flink run    -c org.apache.flink.examples.java.wordcount.WordCount  /home/liwen/flink12/examples/batch/WordCount.jar  
/home/liwen/flink12/bin/flink run -m 11.11.1.79:8081    -c org.apache.flink.examples.java.wordcount.WordCount  /home/liwen/flink12/examples/batch/WordCount.jar --output=hdfs://172.16.101.12:9082/flink/example/out/work.txt

#数据同步实验

/home/liwen/flink12/bin/flink run     -c jrx.data.hub.flink.example.table.SQLExampleData2PG   /home/liwen/flink12/examples/SQLExampleData2PG.jar  
/home/liwen/flink12/bin/flink run  -s hdfs://172.16.101.12:9082/flink/example/out/save.txt   -c jrx.data.hub.flink.example.table.SQLExampleData2PG   /home/liwen/flink12/examples/SQLExampleData2PG.jar  



/home/liwen/flink12/bin/flink list 
  /home/liwen/flink12/bin/flink list -a
  /home/liwen/flink12/bin/flink cancel -s hdfs://172.16.101.12:9082/flink/example/out/save.txt 14709c3ee2be770f8f4140635dec8cc7
  
  
#数据库补数


java -classpath  /home/liwen/flink12/lib  -cp MysqlComplemented.jar jrx.data.hub.flink.example.table.MysqlComplemented
java -jar MysqlComplemented.jar 
/home/liwen/flink12/bin/flink run     -c jrx.data.hub.flink.example.table.MysqlComplemented  /home/liwen/flink12/examples/MysqlComplemented.jar  


java -Xbootclasspath/a: -jar /home/liwen/flink12/examples/MysqlComplemented.jar  


/home/liwen/application/java/bin/java   -XX:+UseG1GC -Xms1024M -Xms1024M -XX:MaxDirectMemorySize=268435458 -XX:MaxMetaspaceSize=268435456 -Dlog.file=/home/liwen/flink12/log/flink-root-taskexecutor-8-localhost.log -Dlog4j.configuration=file:/home/liwen/flink12/conf/log4j.properties -Dlog4j.configurationFile=file:/home/liwen/flink12/conf/log4j.properties -Dlogback.configurationFile=file:/home/liwen/flink12/conf/logback.xml -classpath /home/liwen/flink12/lib/flink-connector-jdbc_2.11-1.12.1.jar:/home/liwen/flink12/lib/flink-csv-1.12.0.jar:/home/liwen/flink12/lib/flink-json-1.12.0.jar:/home/liwen/flink12/lib/flink-shaded-hadoop-2-uber-2.8.3-10.0.jar:/home/liwen/flink12/lib/flink-shaded-zookeeper-3.4.14.jar:/home/liwen/flink12/lib/flink-sql-connector-kafka_2.11-1.12.1.jar:/home/liwen/flink12/lib/flink-sql-connector-mysql-cdc-1.2-SNAPSHOT.jar:/home/liwen/flink12/lib/flink-sql-connector-postgres-cdc-1.2-SNAPSHOT.jar:/home/liwen/flink12/lib/flink-table_2.11-1.12.0.jar:/home/liwen/flink12/lib/flink-table-blink_2.11-1.12.0.jar:/home/liwen/flink12/lib/greenplum-5.18.0.jar:/home/liwen/flink12/lib/log4j-1.2-api-2.12.1.jar:/home/liwen/flink12/lib/log4j-api-2.12.1.jar:/home/liwen/flink12/lib/log4j-core-2.12.1.jar:/home/liwen/flink12/lib/log4j-slf4j-impl-2.12.1.jar:/home/liwen/flink12/lib/mysql-connector-java-5.1.47.jar:/home/liwen/flink12/lib/postgresql-42.2.19.jar:/home/liwen/flink12/lib/flink-dist_2.11-1.12.0.jar:/home/liwen/flink12/examples/MysqlComplemented.jar::: jrx.data.hub.flink.example.table.MysqlComplemented  --script /home/liwen/flink12/examples
 
 
 --configDir /home/liwen/flink12/conf -D taskmanager.memory.framework.off-heap.size=134217728b -D taskmanager.memory.network.max=134217730b -D taskmanager.memory.network.min=134217730b -D taskmanager.memory.framework.heap.size=134217728b -D taskmanager.memory.managed.size=536870920b -D taskmanager.cpu.cores=40.0 -D taskmanager.memory.task.heap.size=402653174b -D taskmanager.memory.task.off-heap.size=0b 
 
 
 
 /home/liwen/flink12/bin/flink run  -m 10.0.8.13:8081  -p 1   -c jrx.data.hub.flink.example.table.MysqlComplemented  /home/liwen/flink12/examples/MysqlComplemented.jar   --script /home/liwen/flink12/examples


#flink12.2
/home/liwen/flink12.2/flink-1.12.2/bin


 /home/liwen/flink12.2/flink-1.12.2/bin/flink run  -m  11.11.1.79:8081  -p 1   -c com.riveretech.est.cdc.JobCdcApp  /home/liwen/flink12.2/jobhome/job-cdc-1.0.0-SNAPSHOT.jar   


/home/liwen/flink12.2/flink-1.12.2/bin/flink run   -c com.riveretech.est.runtime.JobApp  /home/liwen/flink12.2/jobhome/example/job-runtime-1.0.0-SNAPSHOT.jar  --dependency_external /data/Anyestnfs/jarlib



 /home/liwen/flink12.2/flink-1.12.2/bin/flink run -m 11.11.1.79:8081 -C "file:///data/Anyestnfs/jarlib/common-1.0.0-SNAPSHOT.jar" -c com.riveretech.est.runtime.JobApp /home/liwen/flink12.2/jobhome/save_binlog_job/841164a970b24dc584c3cec67a014a31/job-runtime-1.0.0-SNAPSHOT.jar --dependency_external /data/Anyestnfs/jarlib
  /home/liwen/flink12.2/flink-1.12.2/bin/flink run -m 11.11.1.79:8081  -C file:///data/Anyestnfs/jarlib/ -c com.riveretech.est.runtime.JobApp /home/liwen/flink12.2/jobhome/save_binlog_job/841164a970b24dc584c3cec67a014a31/job-runtime-1.0.0-SNAPSHOT.jar --dependency_external /data/Anyestnfs/jarlib
  
  /home/liwen/flink12.2/flink-1.12.2/bin/standalone-job.sh start  -C file:///data/Anyestnfs/jarlib/ --job-classname com.riveretech.est.runtime.JobApp /home/liwen/flink12.2/jobhome/save_binlog_job/841164a970b24dc584c3cec67a014a31/job-runtime-1.0.0-SNAPSHOT.jar