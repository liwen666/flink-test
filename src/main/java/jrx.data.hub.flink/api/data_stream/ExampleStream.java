package jrx.data.hub.flink.api.data_stream;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author LW
 * @since 2021/4/15  17:45
 */
public class ExampleStream {
     final StreamExecutionEnvironment env =
            StreamExecutionEnvironment.getExecutionEnvironment();

    @Before
    public void name() {

    }

    @Test
    public void test() throws Exception {

        List<Person> people = new ArrayList<Person>();

        people.add(new Person("Fred", 35));
        people.add(new Person("Wilma", 35));
        people.add(new Person("Pebbles", 2));

        DataStream<Person> flintstones = env.fromCollection(people);
        flintstones.print();
        env.execute();

    }

    @Test
    public void socket() {
        DataStream<String> lines = env.socketTextStream("11.11.1.79", 9999);
    }

    @Test
    public void fileStream() {
        DataStream<String> lines = env.readTextFile("file:///D:\\flink12\\flink-1.12.1-src\\flink-engine\\src\\main\\java\\jrx.data.hub.flink\\api\\data_stream\\demo.txt");

    }
}
