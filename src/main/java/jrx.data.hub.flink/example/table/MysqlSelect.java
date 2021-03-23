package jrx.data.hub.flink.example.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author LW
 * @since 2021/3/20  15:39
 */
public class MysqlSelect {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        blinkStreamEnv.setParallelism(1);
    EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
            .useBlinkPlanner()
            .inStreamingMode()
            .build();
    StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);

    String create = "create table data_select(\n" +
            "`object_field_id` INT,\n" +
            "`content_code` STRING,\n" +
            "`create_person` STRING,\n" +
            "`create_time` TIMESTAMP,\n" +
            "`update_person` STRING,\n" +
            "`update_time` TIMESTAMP,\n" +
            "`column_code` STRING,\n" +
            "`compute_period` STRING,\n" +
            "`data_object_id` INT,\n" +
            "`default_value` STRING,\n" +
            "`description` STRING,\n" +
            "`field_code` STRING,\n" +
            "`field_format` STRING,\n" +
            "`field_length` INT,\n" +
            "`field_name` STRING,\n" +
            "`field_state` STRING,\n" +
            "`field_type` STRING,\n" +
            "`list_value_type` STRING,\n" +
            "`object_type` STRING,\n" +
            "`object_versions` STRING,\n" +
            "`refer_field_ids` STRING,\n" +
            "`resource_object_category_id` INT,\n" +
            "`resource_object_id` INT,\n" +
            "`resource_object_version_id` INT,\n" +
            "`scale_length` INT,\n" +
            "`source_field_id` INT,\n" +
            "`sql_fragment` STRING,\n" +
            "`update_mode` STRING,\n" +
            "`value_type` STRING\n" +
            ")WITH (\n" +
            "    'connector.type' = 'jdbc',\n" +
            "    'connector.driver' = 'com.mysql.cj.jdbc.Driver',\n" +
            "    'connector.url' = 'jdbc:mysql://11.11.1.79:3306/anyest3_financial_cloud_101?serverTimezone=Hongkong&useUnicode=true&useSSL=false&characterEncoding=utf8', \n" +
            "    'connector.table' = 'meta_object_field', \n" +
            "    'connector.username' = 'root', \n" +
            "    'connector.password' = 'root',\n" +
            "    'connector.write.flush.max-rows' = '1' \n" +
            ")";
    String sql = "select * from data_select limit 1 ";
        blinkStreamTableEnv.executeSql(create);
        TableResult tableResult = blinkStreamTableEnv.executeSql(sql);
        tableResult.print();
        try {
        blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
    } catch (Exception e) {
        System.err.println(e);
    }
}
}
