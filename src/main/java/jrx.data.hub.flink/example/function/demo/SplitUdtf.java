package jrx.data.hub.flink.example.function.demo;

import org.apache.flink.table.functions.TableFunction;

/**
 *
 */
public class SplitUdtf extends TableFunction<String> {
    public void eval(String str) {
        String[] split = str.split("\\|");
        for (String s : split) {
            collect(s);
        }
    }
}
