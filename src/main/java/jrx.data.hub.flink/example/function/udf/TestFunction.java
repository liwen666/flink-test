package jrx.data.hub.flink.example.function.udf;

import org.apache.flink.table.functions.ScalarFunction;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;

/**
 * @Author jie.lan
 * @Date 2021/3/22 20:07
 */
public class TestFunction extends ScalarFunction {
    public Integer eval(Integer interval) {
        if (interval != null) {
            return interval * 2;
        }
        return null;

    }


}
