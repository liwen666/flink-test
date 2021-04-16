package jrx.data.hub.flink.api.base;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author LW
 * @since 2021/4/15  18:06
 */
public class NYCFilter implements org.apache.flink.api.common.functions.FilterFunction<TaxiRide> {
    @Override
    public boolean filter(TaxiRide value) throws Exception {
        return true;
    }
}
