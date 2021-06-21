package jrx.data.hub.flink.example.query;

import org.apache.flink.table.functions.ScalarFunction;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @Author jie.lan
 * @Date 2021/3/22 20:07
 */
public class ConvertDate  extends ScalarFunction {
    public Date eval(Integer interval) {
        if(interval!=null){
            return new Date(interval*24*60*60*1000l);
        }
        return null;

    }
    public Timestamp eval(String timestamp ) {
        return null;

    }
    public Timestamp eval(Long unixtime ) {
        if(unixtime!=null){
            return new Timestamp(unixtime);
        }
        return null;

    }
    public Timestamp eval(Timestamp timestamp ) {
        return timestamp;

    }
    public Date eval(Date date ) {
        return date;

    }
}
