package jrx.data.hub.flink.api.base;


import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author LW
 * @since 2021/4/15  17:59
 */
@Getter
@Setter
public class TaxiRide {
    protected Object rideId
            ;
    protected Object isStart;
    protected Object startLon;
    protected Object startLat;
    protected Object endLon;
    protected Object endLat;
}
