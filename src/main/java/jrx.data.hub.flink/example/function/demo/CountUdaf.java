package jrx.data.hub.flink.example.function.demo;

import org.apache.flink.table.functions.AggregateFunction;

/* @param <T> UDAF的输出结果的类型。
 * @param <ACC> UDAF的accumulator的类型。accumulator是UDAF计算中用来存放计算中间结果的数据类型。您可以需要根据需要自行设计每个UDAF的accumulator。*/

/**
 *
 */
public class CountUdaf extends AggregateFunction<Long, CountUdaf.CountAccum> {
    //定义一个Accumulator，存放聚合的中间结果。

    /**
     *
     */
    public static class CountAccum {
        public long total;
    }

    //初始化count UDAF的accumulator。
    public CountAccum createAccumulator() {
        CountAccum acc = new CountAccum();
        acc.total = 0;
        return acc;
    }

    //返回聚合的最终结果
    public Long getValue(CountAccum accumulator) {
        return accumulator.total;
    }

    //accumulate提供了，如何根据输入的数据，更新count UDAF存放状态的accumulator。
    public void accumulate(CountAccum accumulator, Object iValue) {
        accumulator.total++;
    }

    public void merge(CountAccum accumulator, Iterable<CountAccum> its) {
        for (CountAccum other : its) {
            accumulator.total += other.total;
        }
    }
}

