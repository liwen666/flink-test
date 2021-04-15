package jrx.data.hub.flink.example.cdc.multiple;

import com.alibaba.ververica.cdc.connectors.mysql.MySQLSource;
import com.alibaba.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.alibaba.ververica.cdc.debezium.DebeziumSourceFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.kafka.shaded.org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper;

import java.util.Properties;

/**
 *
 */
public class FlinkCdc {

    public static void main(String[] args) throws Exception {
        //TODO 1.获取流处理执行环境
//        Configuration configuration = new Configuration();
//        configuration.setString(ConfigConstants.SAVEPOINT_DIRECTORY_KEY, "file:\\C:\\Users\\liwen\\Desktop\\backend\\save");
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(configuration);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        EnvironmentSettings settings = EnvironmentSettings.newInstance().build();
//        StreamTableEnvironment.create(env,settings);
        env.enableCheckpointing(100000L); //5s执行一次Checkpoint
        // 设置Checkpoint的模式：精准一次
//        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // 确保检查点之间有至少1s的间隔【checkpoint最小间隔】
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
        // 检查点必须在一分钟内完成，或者被丢弃【checkpoint的超时时间】
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        // 同一时间只允许进行一个检查点
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //任务挂掉的时候是否清理checkpoint。使任务正常退出时不删除CK内容，有助于任务恢复。默认的是取消的时候清空checkpoint中的数据。RETAIN_ON_CANCELLATION表示取消任务的时候，保存最后一次的checkpoint。便于任务的重启和恢复，正常情况下都使用RETAIN
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//        //设置一个重启策略：默认的固定延时重启次数，重启的次数是Integer的最大值，重启的间隔是1s
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(500, 10000L));
        env.setStateBackend(new FsStateBackend("file:\\C:\\Users\\liwen\\Desktop\\backend\\check"));

//      //指定用户
//        System.setProperty("HADOOP_USER_NAME", "atguigu");

        //TODO 2.读取mysql变化数据 监控MySQL中变化的数据
        String table = "flink_web.cdc_test,flink_web.data_info";
        DebeziumSourceFunction<String> sourceFunction = MySQLSource.<String>builder() //使用builder创建MySQLsource对象，需要指定对象的泛型。
                .hostname("11.11.1.79") //指定监控的哪台服务器（MySQL安装的位置）
                .port(3306) //MySQL连接的端口号
                .username("root") //用户
                .password("root")//密码
                .serverId(20)
                .databaseList("flink_web") //list：可以监控多个库
                .tableList("flink_web.cdc_test,flink_web.data_info") //如果不写则监控库下的所有表，需要使用【库名.表名】
                //debezium中有很多配置信息。可以创建一个对象来接收
                //.debeziumProperties(properties)
                .deserializer(new JsonDebeziumDeserializeSchema())
//                .startupOptions(StartupOptions.specificOffset("mysql-bin.000018",22128859)) //初始化数据：空值读不读数据库中的历史数据。initial（历史+连接之后的）、latest-offset（连接之后的）。timestamp（根据指定时间戳作为开始读取的位置）
                .startupOptions(StartupOptions.initial())
                .build();

        Properties props = new Properties();
        // 设置了retries参数，可以在Kafka的Partition发生leader切换时，Flink不重启，而是做5次尝试：
        props.setProperty(ProducerConfig.RETRIES_CONFIG, "5");
        props.setProperty(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, String.valueOf(1048576 * 5));
        // 设置事务id
        props.put("transactional.id", "first-transactional");
        // 设置幂等性
        props.put("enable.idempotence",true);
        //Set acknowledgements for producer requests.
        props.put("acks", "all");
        //If the request fails, the producer can automatically retry,
        props.put("retries", 1);
        //Specify buffer size in config,这里不进行设置这个属性,如果设置了,还需要执行producer.flush()来把缓存中消息发送出去
        props.put("batch.size", 16384);
        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);
        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);
        // Kafka消息是以键值对的形式发送,需要设置key和value类型序列化器
        props.put("bootstrap.servers", "11.11.1.79:9092");
        props.put("transaction.timeout.ms", 360000);
        props.put("group.id", "test_cdc");
//        props.put(Constants.KEY_SERIALIZER ,"org.apache.flink.kafka.shaded.org.apache.kafka.common.serialization.ByteArraySerializer");
//        props.put(Constants.VALUE_SERIALIZER ,"org.apache.flink.kafka.shaded.org.apache.kafka.common.serialization.ByteArraySerializer");
//        props.put("zookeeper.connect", "localhost:2181");
        FlinkKafkaProducer producer = new FlinkKafkaProducer(
                "myData",
                new MyKafkaSerializationSchema(), props, FlinkKafkaProducer.Semantic.EXACTLY_ONCE);
        producer.setWriteTimestampToKafka(true);
        DataStreamSink streamSource = env.addSource(sourceFunction)
                .addSink(producer).setParallelism(1).name("toKafka");
        //TODO 4.启动任务
        env.execute("flink cdc job");
    }
}
