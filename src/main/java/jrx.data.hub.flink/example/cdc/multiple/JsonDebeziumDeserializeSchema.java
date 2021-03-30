package jrx.data.hub.flink.example.cdc.multiple;

import com.alibaba.ververica.cdc.debezium.DebeziumDeserializationSchema;
import io.debezium.data.Envelope;
import jrx.data.hub.core.utils.JsonUtils;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * 对binlog解析的数据处理
 */
public class JsonDebeziumDeserializeSchema implements DebeziumDeserializationSchema<String> {
    @Override
    public void deserialize(SourceRecord sourceRecord, Collector collector) throws Exception {
        //定义JSON对象用于存放反序列化后的数据
        Map result = new HashMap<>();
        //获取库名和表名
        String topic = sourceRecord.topic();
        String[] split = topic.split("\\.");
        String database = split[1];
        String table = split[2];
        //获取操作类型
        Envelope.Operation operation = Envelope.operationFor(sourceRecord);
        //取监控表的主键
        Struct keyStruck = (Struct) sourceRecord.key();
        StringBuilder kafkaKey = new StringBuilder();
        if (keyStruck.schema() != null) {
            for (Field field : keyStruck.schema().fields()) {
                kafkaKey.append(keyStruck.get(field));
            }
        }
        //获取数据本身
        Struct struct = (Struct) sourceRecord.value();

        Struct after = struct.getStruct("after");
        Map afterValue = new HashMap();
        if (after != null) {
            Schema schema = after.schema();
            for (Field field : schema.fields()) {
                afterValue.put(field.name(), after.get(field.name()));
            }
        }
        Struct before = struct.getStruct("before");
        Map beforeValue = new HashMap();
        if (before != null) {
            Schema schema = before.schema();
            for (Field field : schema.fields()) {
                beforeValue.put(field.name(), before.get(field.name()));
            }
            beforeValue.put("ts_ms", struct.getInt64("ts_ms"));
        }

        Struct source = struct.getStruct("source");
        Map sourceValue = new HashMap();
        if (source != null) {
//            afterValue.put("ts_ms", source.getInt64("ts_ms"));
            afterValue.put("ts_ms", struct.getInt64("ts_ms"));
            Schema schema = source.schema();
            for (Field field : schema.fields()) {
                sourceValue.put(field.name(), source.get(field.name()));
            }
        }
        result.put("after", afterValue);
        result.put("before", beforeValue);
        result.put("source", sourceValue);
        result.put("op", operation.code());
        result.put("routeKey", kafkaKey);
        result.put("topic", database + "." + table);
        //将数据传输出去

        collector.collect(JsonUtils.obj2String(result));
    }

    @Override
    public TypeInformation getProducedType() {
//        return BasicTypeInfo.STRING_TYPE_INFO;
        return TypeInformation.of(String.class);
    }

}
