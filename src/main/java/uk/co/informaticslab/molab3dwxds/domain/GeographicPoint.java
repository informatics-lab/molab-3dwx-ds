package uk.co.informaticslab.molab3dwxds.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by tom on 17/06/2015.
 */
public class GeographicPoint {

    private double lat;
    private double lng;

    public GeographicPoint() {

    }

    @JsonCreator
    public GeographicPoint(@JsonProperty(Constants.LAT) double lat,
                           @JsonProperty(Constants.LNG) double lng) {
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
