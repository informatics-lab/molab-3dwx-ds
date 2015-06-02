package uk.co.informaticslab.molab3dwxds.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

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
                 DataDimensions dataDimensions) {
        super(data, mimeType, resolution, model, forecastReferenceTime, phenomenon, dataDimensions);
    }

    @Override
    public String toString() {
        return "Video{} " + super.toString();
    }
}
