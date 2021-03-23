package jrx.data.hub.flink.example.function.demo3;

import org.apache.flink.table.functions.ScalarFunction;

/**
 * 比较2个数最大值
 *
 */
public class Max2Function2 extends ScalarFunction {
    /**
     * 比较2个数最大值
     *
     */
    public Integer eval(Integer num1, Integer num2) {
        return Math.min(num1, num2);
    }

}
