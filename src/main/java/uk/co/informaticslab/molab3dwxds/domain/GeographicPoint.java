package uk.co.informaticslab.molab3dwxds.domain;

/**
 * Created by tom on 17/06/2015.
 */
public class GeographicPoint {

    private final double lat;
    private final double lng;

    public GeographicPoint(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

}
