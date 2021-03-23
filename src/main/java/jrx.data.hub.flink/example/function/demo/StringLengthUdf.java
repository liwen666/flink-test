package jrx.data.hub.flink.example.function.demo;

import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.table.functions.ScalarFunction;

/**
 *
 */
public class StringLengthUdf extends ScalarFunction {
    @Override
    public void open(FunctionContext context) {
    }

    //eval为函数调用的主要方法，方法可重载
    public long eval(String a) {
        return a == null ? 0 : a.length();
    }

    public long eval(String b, String c) {
        return eval(b) + eval(c);
    }

    @Override
    public void close() {
    }
}
