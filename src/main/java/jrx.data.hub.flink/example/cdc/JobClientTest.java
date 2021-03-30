package jrx.data.hub.flink.example.cdc;

import com.alibaba.ververica.cdc.connectors.mysql.MySQLSource;
import com.alibaba.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.alibaba.ververica.cdc.debezium.DebeziumSourceFunction;
import jrx.data.hub.flink.example.cdc.multiple.JsonDebeziumDeserializeSchema;
import jrx.data.hub.flink.example.cdc.multiple.MyKafkaSerializationSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

/**
 *
 */
public class JobClientTest {

    public static void main(String[] args) throws Exception {
        //TODO 1.获取流处理执行环境
        Configuration config = new Configuration();
        config.setString("state.checkpoints.dir", "D:\\flink12\\flink-1.12.1-src\\flink-engine\\src\\main\\java\\jrx.data.hub.flink\\example\\cdc\\multiple");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(1);
//        //1.1Checkpoint相关
//        /*读取的是binlog中的数据，如果集群挂掉，尽量能实现断点续传功能。如果从最新的读取（丢数据）。如果从最开始读（重复数据）。理想状态：读取binlog中的数据读一行，保存一次读取到的（读取到的行）位置信息。而flink中读取行位置信息保存在Checkpoint中。使用Checkpoint可以把flink中读取（按行）的位置信息保存在Checkpoint中*/
//        env.enableCheckpointing(50000L); //5s执行一次Checkpoint
//        //设置Checkpoint的模式：精准一次
//        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
//        // 确保检查点之间有至少1s的间隔【checkpoint最小间隔】
//        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
//// 检查点必须在一分钟内完成，或者被丢弃【checkpoint的超时时间】
//        env.getCheckpointConfig().setCheckpointTimeout(60000);
//// 同一时间只允许进行一个检查点
//        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
//        //任务挂掉的时候是否清理checkpoint。使任务正常退出时不删除CK内容，有助于任务恢复。默认的是取消的时候清空checkpoint中的数据。RETAIN_ON_CANCELLATION表示取消任务的时候，保存最后一次的checkpoint。便于任务的重启和恢复，正常情况下都使用RETAIN
//        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//        //设置一个重启策略：默认的固定延时重启次数，重启的次数是Integer的最大值，重启的间隔是1s
//        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(5, 10000L));
//        //设置一个状态后端 jobManager。如果使用的yarn集群模式，jobManager随着任务的生成而生成，任务挂了jobManager就没了。因此需要启动一个状态后端。只要设置checkpoint，尽量就设置一个状态后端。保存在各个节点都能读取的位置：hdfs中
////        env.setStateBackend(new FsStateBackend("file:\\/c:\\/flink-checkpoints\\/backend"));
//        env.setStateBackend(new MemoryStateBackend());
        //指定用户
//        System.setProperty("HADOOP_USER_NAME", "atguigu");

        //TODO 2.读取mysql变化数据 监控MySQL中变化的数据
        Properties properties = new Properties(); //创建一个变量可以添加之后想添加的配置信息
        String table = "flink_web.cdc_test,flink_web.data_info";
        DebeziumSourceFunction<String> sourceFunction = MySQLSource.<String>builder() //使用builder创建MySQLsource对象，需要指定对象的泛型。
                .hostname("11.11.1.79") //指定监控的哪台服务器（MySQL安装的位置）
                .port(3306) //MySQL连接的端口号
                .username("root") //用户
                .password("root")//密码
                .serverId(20)
                .databaseList("flink_web") //list：可以监控多个库
                .tableList("flink_web.user_log") //如果不写则监控库下的所有表，需要使用【库名.表名】
                //debezium中有很多配置信息。可以创建一个对象来接收
                //.debeziumProperties(properties)
                .deserializer(new JsonDebeziumDeserializeSchema())
//                .startupOptions(StartupOptions.specificOffset("mysql-bin.000018",22128859)) //初始化数据：空值读不读数据库中的历史数据。initial（历史+连接之后的）、latest-offset（连接之后的）。timestamp（根据指定时间戳作为开始读取的位置）
                .startupOptions(StartupOptions.initial())
                .build();

        Properties props = new Properties();
        props.put("bootstrap.servers", "11.11.1.79:9092");
        props.put("transaction.timeout.ms", 360000);
        props.put("group.id", "data_1");
//        props.put("zookeeper.connect", "localhost:2181");

        DataStreamSink streamSource = env.addSource(sourceFunction)
                .addSink(new FlinkKafkaProducer(
                        "myData",
                        new MyKafkaSerializationSchema(), props, FlinkKafkaProducer.Semantic.EXACTLY_ONCE
                )).setParallelism(1).name("toKafka");
        //TODO 4.启动任务
        env.execute("flink cdc job");
    }
}
