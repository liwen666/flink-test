package jrx.data.hub.flink.api.base;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author LW
 * @since 2021/4/15  18:04
 */
public class TaxiRideSource implements SourceFunction {
    private static final long serialVersionUID = 1L;

    private volatile boolean isRunning = true;
    private int counter = 10000;
    private int start = 0;

    private ListState<Integer> state;

    public TaxiRideSource() {

    }

    @Override
    public void run(SourceContext ctx) throws Exception {
        while ((start < counter || counter == -1) && isRunning) {
            synchronized (ctx.getCheckpointLock()) {
                ctx.collect(start);
                ++start;

                // loop back to 0
                if (start == Integer.MAX_VALUE) {
                    start = 0;
                }
            }
            Thread.sleep(10L);
        }
    }

    @Override
    public void cancel() {

    }
}
