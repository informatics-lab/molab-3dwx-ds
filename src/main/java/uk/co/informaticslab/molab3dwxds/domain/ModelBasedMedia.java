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
    private String processingProfile;

    public ModelBasedMedia() {
    }

    public ModelBasedMedia(byte[] data,
                           String mimeType,
                           Resolution resolution,
                           String model,
                           DateTime forecastReferenceTime,
                           String phenomenon,
                           DataDimensions dataDimensions,
                           List<GeographicPoint> geographicRegion,
                           String processingProfile) {
        super(data, mimeType, resolution);
        this.model = model;
        this.forecastReferenceTime = forecastReferenceTime;
        this.phenomenon = phenomenon;
        this.dataDimensions = dataDimensions;
        this.geographicRegion = geographicRegion;
        this.processingProfile = processingProfile;
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

    public String getProcessingProfile() {
        return processingProfile;
    }

    @Override
    public String toString() {
        return "ModelBasedMedia{" +
                "model='" + model + '\'' +
                ", forecastReferenceTime=" + forecastReferenceTime +
                ", phenomenon='" + phenomenon + '\'' +
                ", dataDimensions=" + dataDimensions +
                ", geographicRegion=" + geographicRegion +
                ", processingProfile='" + processingProfile + '\'' +
                "} " + super.toString();
    }
}
