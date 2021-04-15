package jrx.data.hub.flink.example.cdc.multiple;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author LW
 * @since 2021/3/29  14:13
 */
public class Constants {
    /**
     * 外部配置文件地址
     */
    public static final String EXTERNAL_FILE_PATH = "external_file_path";
    /**
     * 指定配置的版本
     */
    public static final String CDC_CONFIG_VERSION = "cdc_config_version";
    /**
     * 扩展依赖jar
     */
    public static final String DEPENDENCY_EXTERNAL = "dependency_external";

    /**
     * 数据解析魔法值设置
     */
    public static final String AFTER = "after";

    public static final String BEFORE = "before";
    public static final String SOURCE = "source";
    public static final String OP = "op";
    public static final String ROUTE_KEY = "routeKey";
    public static final String TOPIC = "topic";
    public static final String TS_MS = "ts_ms";

    /**
     * kafka配置
     */
    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";
    public static final String TRANSACTION_TIMEOUT_MS = "transaction.timeout.ms";
    public static final String GROUP_ID = "group.id";
    public static final String KEY_SERIALIZER = "key.serializer";
    public static final String VALUE_SERIALIZER = "value.serializer";

    /**
     * 配置文件名
     */
    public static final String CONFIG_FILE_NAME = "job.json";


}
