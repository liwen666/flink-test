package jrx.data.hub.flink.example.function.demo2;

import org.apache.flink.table.functions.TableFunction;

/**
 * 接收不固定个数的int型参数,然后将所有数据依次返回
 */
public class FlattenFunction extends TableFunction<Integer> {
    public void eval(Integer... args) {
        for (Integer i : args) {
            collect(i);
        }
    }

    /**
     * 注册多个eval方法，接收long或者string类型的参数，然后将他们转成string类型
     */
    public static class DuplicatorFunction extends TableFunction<String> {
        public void eval(Long i) {
            eval(String.valueOf(i));
        }

        public void eval(String s) {
            collect(s);
        }
    }
}
