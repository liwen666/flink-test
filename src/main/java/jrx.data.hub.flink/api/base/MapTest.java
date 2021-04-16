package jrx.data.hub.flink.api.base;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author LW
 * @since 2021/4/15  18:04
 */
public class MapTest {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource dataStreamSource = env.addSource(new TaxiRideSource(), "aaaa", new TypeInformationTest());

        DataStream<EnrichedRide> enrichedNYCRides = dataStreamSource
                .filter(new NYCFilter())
                .map(new Enrichment());

        enrichedNYCRides.print();
        env.execute();
    }
    public static class Enrichment implements MapFunction<TaxiRide, EnrichedRide> {

        @Override
        public EnrichedRide map(TaxiRide taxiRide) throws Exception {
            return new EnrichedRide(taxiRide);
        }
    }
}

