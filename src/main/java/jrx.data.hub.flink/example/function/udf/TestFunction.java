package jrx.data.hub.flink.example.function.udf;

import org.apache.flink.table.functions.ScalarFunction;

/**
 * <p>
 *  描述
 * </p>
 *
 * @author lw
 * @since  2021/4/20 15:01
 */

public class TestFunction extends ScalarFunction {
    public Integer eval(Integer interval) {
        if (interval != null) {
            return interval * 2;
        }
        return null;

    }


}
