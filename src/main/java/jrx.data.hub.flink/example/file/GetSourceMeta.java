package jrx.data.hub.flink.example.file;

import com.alibaba.fastjson.JSON;
import org.apache.flink.table.functions.ScalarFunction;

/**
 * @Author jie.lan
 * @Date 2021/3/25 15:01
 *
 * 抽取debezium_json source中的数据
 */
public class GetSourceMeta extends ScalarFunction {
    public String eval(String debeziumJson,String key ) {
        return JSON.parseObject(debeziumJson).getJSONObject("source").getString(key);
    }
}
