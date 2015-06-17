package uk.co.informaticslab.molab3dwxds.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * POJO for a video
 */

@Document(collection = "videos")
public class Video extends ModelBasedMedia {

    public Video() {
    }

    @PersistenceConstructor
    public Video(byte[] data,
                 String mimeType,
                 Resolution resolution,
                 String model,
                 DateTime forecastReferenceTime,
                 String phenomenon,
                 DataDimensions dataDimensions,
                 List<GeographicPoint> geographicRegion) {
        super(data, mimeType, resolution, model, forecastReferenceTime, phenomenon, dataDimensions, geographicRegion);
    }

    @Override
    public String toString() {
        return "Video{} " + super.toString();
    }
}
