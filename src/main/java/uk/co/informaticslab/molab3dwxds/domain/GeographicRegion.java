package uk.co.informaticslab.molab3dwxds.domain;

import java.util.List;

/**
 * Created by tom on 17/06/2015.
 */
public class GeographicRegion {

    private final List<GeographicPoint> points;

    public GeographicRegion(List<GeographicPoint> points) {
        if (points.size() == 4) {
            this.points = points;
        } else {
            throw new IllegalArgumentException("4 geographic points are required to construct a geographic region.");
        }
    }

    public List<GeographicPoint> getPoints() {
        return points;
    }

}
