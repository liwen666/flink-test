package jrx.data.hub.flink.api.base;

public class EnrichedRide extends TaxiRide {
    private Object rideId;
    public int startCell;
    public int endCell;

    public EnrichedRide() {
    }

    public EnrichedRide(TaxiRide ride) {
        this.rideId = ride.rideId;
        this.isStart = ride.isStart;
        this.startCell = GeoUtils.mapToGridCell(ride.startLon, ride.startLat);
        this.endCell = GeoUtils.mapToGridCell(ride.endLon, ride.endLat);
    }

    public String toString() {
        return super.toString() + "," +
                Integer.toString(this.startCell) + "," +
                Integer.toString(this.endCell);
    }
}