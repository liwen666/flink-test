package jrx.data.hub.flink.api.water;

import jrx.data.hub.flink.api.water.MyEvent;
import org.apache.flink.api.common.io.FileInputFormat;
import org.apache.flink.api.common.io.FilePathFilter;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.FileProcessingMode;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;

import static java.util.logging.Level.WARNING;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author LW
 * @since 2021/4/15  17:38
 */
public class WaterMark {
    public static void main(String[] args) {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    }
}
