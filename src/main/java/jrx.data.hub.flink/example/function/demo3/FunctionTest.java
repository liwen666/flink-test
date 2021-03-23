package jrx.data.hub.flink.example.function.demo3;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.Arrays;

/**
 * @program: strategy-topology
 * @description:
 * @author: Alexander
 * @create: 2021-03-20 14:17
 **/
public class FunctionTest {

    public static void main(String[] args) throws Exception {

        // set up execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);
        DataStream<Order> orderA = env.fromCollection(Arrays.asList(
                new Order(1L, "beer", 3),
                new Order(1L, "diaper", 4),
                new Order(3L, "rubber", 2)));

        // register DataStream as Table
        tEnv.registerDataStream("OrderA", orderA, "user, product, amount");
//            tEnv.createTemporaryFunction(
//                    "max2",
//                    Max2Function.class
//            );
//            Table result = tEnv.sqlQuery("SELECT max2(1,7) FROM OrderA WHERE user < 3");
//
//            tEnv.toAppendStream(result, Integer.class).print();

        tEnv.createTemporaryFunction(
                "max2",
                Max2Function2.class
        );
        Table result2 = tEnv.sqlQuery("SELECT max2(1,7) FROM OrderA WHERE user < 3");
        tEnv.toAppendStream(result2, Integer.class).print();

        env.execute();

    }

    /**
     * Simple POJO.
     */
    public static class Order {
        public Long user;
        public String product;
        public int amount;

        public Order() {
        }

        public Order(Long user, String product, int amount) {
            this.user = user;
            this.product = product;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "user=" + user +
                    ", product='" + product + '\'' +
                    ", amount=" + amount +
                    '}';
        }
    }

}


