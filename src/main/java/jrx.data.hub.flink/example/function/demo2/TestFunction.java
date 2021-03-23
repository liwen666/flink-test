package jrx.data.hub.flink.example.function.demo2;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author LW
 * @since 2021/3/19  17:55
 */
public class TestFunction {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings bsSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, bsSettings);
//
//        tEnv.registerFunction("split", new FlattenFunction.Split(" "));
//        tEnv.registerFunction("duplicator", new DuplicatorFunction());
//        tEnv.registerFunction("flatten", new FlattenFunction());

        tEnv.createTemporaryFunction("split", Split.class);
        tEnv.createTemporaryFunction("duplicator", DuplicatorFunction.class);
        tEnv.createTemporaryFunction("flatten", FlattenFunction.class);
        /**
         * 构造数据源
         */
        List<Tuple2<Long, String>> ordersData = new ArrayList<>();
        ordersData.add(Tuple2.of(200000000L, "Euro"));
        ordersData.add(Tuple2.of(1000000000L, "US Dollar"));
        ordersData.add(Tuple2.of(5000000000L, "Yen"));
        ordersData.add(Tuple2.of(300000000L, "Euro"));

        DataStream<Tuple2<Long, String>> ordersDataStream = env.fromCollection(ordersData);
        Table orders = tEnv.fromDataStream(ordersDataStream, "amount, currency");
        tEnv.registerTable("Orders", orders);

//        join 和left join不一样    String sql = "SELECT o.currency, T.word, T.length FROM Orders as o ," +
//               " LATERAL TABLE(split(currency)) as T(word, length)";

//        多种类型参数
//        String sql2 = "SELECT * FROM Orders as o , " +
//            "LATERAL TABLE(duplicator(amount))," +
//            "LATERAL TABLE(duplicator(currency))";

//        String sql3 = "SELECT * FROM Orders as o , " +
//            "LATERAL TABLE(flatten(100,200,300))";

//        Table result = tEnv.sqlQuery(
//            "SELECT o.currency, T.word, T.length FROM Orders as o LEFT JOIN LATERAL TABLE(split(currency)) as T(word, length) ON TRUE");

        Table result = tEnv.sqlQuery(
                "SELECT * FROM Orders"
        );
//        tEnv.toAppendStream(result, Row.class).print();
        tEnv.toAppendStream(result, TypeInformation.of(new TypeHint<Tuple2<Long, String>>() {
        })).print();

        Table result1 = tEnv.sqlQuery(
                "SELECT split(currency) FROM Orders"
        );
//        tEnv.toAppendStream(result, Row.class).print();
        env.execute();
    }
}
