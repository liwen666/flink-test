package jrx.data.hub.flink.example.cdc.multiple;

import jrx.data.hub.core.utils.JsonUtils;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 根据数据分topic发送
 */
public class MyKafkaSerializationSchema implements KafkaSerializationSchema<String> {
    @Override
    public ProducerRecord<byte[], byte[]> serialize(String s, @Nullable Long aLong) {
        Map jsonObject = JsonUtils.string2Obj(s,Map.class);
        String topic = (String) jsonObject.get("topic");
        String key = (String) jsonObject.get("routeKey");
        jsonObject.put("topic",null);
        jsonObject.put("routeKey",null);
        return new ProducerRecord(topic, key.getBytes(StandardCharsets.UTF_8), s.getBytes(StandardCharsets.UTF_8));

    }
}
