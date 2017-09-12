package edu.sag.models;

/**
 * Created by wmsze on 15.08.2017.
 */
public class Location {
    private double lon;
    private double lat;

    public Location(){};

    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
