package jrx.data.hub.flink.example.function.demo2;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;

/**
 *
 */
@FunctionHint(output = @DataTypeHint("ROW< i INT, s STRING >"))
public class DuplicatorFunction extends TableFunction<Row> {
    public void eval(long i, String s) {
        collect(Row.of(i, s));
        collect(Row.of(i, s));
    }
}
