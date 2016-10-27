package model;

/**
 * Created by Sebastian Urbanek on 26.10.16.
 */
public class Cell {

    private final float lat1, lng1, lat2, lng2;
    private final int vehicle_count, average_velocity;

    public Cell(float lat1, float lng1, float lat2, float lng2, int vehicle_count, int average_velocity) {
        this.lat1 = lat1;
        this.lng1 = lng1;
        this.lat2 = lat2;
        this.lng2 = lng2;
        this.vehicle_count = vehicle_count;
        this.average_velocity = average_velocity;
    }

    public float getLat1() {
        return lat1;
    }

    public float getLng1() {
        return lng1;
    }

    public float getLat2() {
        return lat2;
    }

    public float getLng2() {
        return lng2;
    }

    public int getVehicle_count() {
        return vehicle_count;
    }

    public int getAverage_velocity() {
        return average_velocity;
    }
}
