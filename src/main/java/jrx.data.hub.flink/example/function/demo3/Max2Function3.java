package jrx.data.hub.flink.example.function.demo3;

import org.apache.flink.table.functions.ScalarFunction;

/**
 * 比较2个数最大值
 *
 * @author System-EST
 * @create 2021-03-16T23:14:42
 */
public class Max2Function3 extends ScalarFunction {
    /**
     *
     * @param num1 数字1
     * @author System-EST
     * @create 2021-03-16T23:14:42
     */
    public Integer eval(Integer num1) {
        return num1+1;
    }


}
