package uk.co.informaticslab.molab3dwxds.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * POJO for an image
 */
@Document(collection = "images")
public class Image extends ModelBasedMedia {

    private DateTime forecastTime;

    public Image() {
    }

    @PersistenceConstructor
    public Image(byte[] data,
                 String mimeType,
                 Resolution resolution,
                 String model,
                 DateTime forecastReferenceTime,
                 String phenomenon,
                 DataDimensions dataDimensions,
                 DateTime forecastTime) {
        super(data, mimeType, resolution, model, forecastReferenceTime, phenomenon, dataDimensions);
        this.forecastTime = forecastTime;
    }

    public DateTime getForecastTime() {
        return forecastTime;
    }

    @Override
    public String toString() {
        return "Image{" +
                "forecastTime=" + forecastTime +
                "} " + super.toString();
    }
}
