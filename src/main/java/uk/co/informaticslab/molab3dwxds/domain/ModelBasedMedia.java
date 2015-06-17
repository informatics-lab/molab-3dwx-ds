package uk.co.informaticslab.molab3dwxds.domain;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by tom on 29/05/2015.
 */
public abstract class ModelBasedMedia extends Media {

    private String model;
    private DateTime forecastReferenceTime;
    private String phenomenon;
    private DataDimensions dataDimensions;
    private List<GeographicPoint> geographicRegion;

    public ModelBasedMedia() {
    }

    public ModelBasedMedia(byte[] data,
                           String mimeType,
                           Resolution resolution,
                           String model,
                           DateTime forecastReferenceTime,
                           String phenomenon,
                           DataDimensions dataDimensions,
                           List<GeographicPoint> geographicRegion) {
        super(data, mimeType, resolution);
        this.model = model;
        this.forecastReferenceTime = forecastReferenceTime;
        this.phenomenon = phenomenon;
        this.dataDimensions = dataDimensions;
        this.geographicRegion = geographicRegion;
    }

    public String getModel() {
        return model;
    }

    public DateTime getForecastReferenceTime() {
        return forecastReferenceTime;
    }

    public String getPhenomenon() {
        return phenomenon;
    }

    public DataDimensions getDataDimensions() {
        return dataDimensions;
    }

    public List<GeographicPoint> getGeographicRegion() {
        return geographicRegion;
    }

    @Override
    public String toString() {
        return "ModelBasedMedia{" +
                "model='" + model + '\'' +
                ", forecastReferenceTime=" + forecastReferenceTime +
                ", phenomenon='" + phenomenon + '\'' +
                ", dataDimensions=" + dataDimensions +
                ", geographicRegion=" + geographicRegion +
                "} " + super.toString();
    }
}
