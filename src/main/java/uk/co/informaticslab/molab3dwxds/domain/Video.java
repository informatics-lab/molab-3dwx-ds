package uk.co.informaticslab.molab3dwxds.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * POJO for a video
 */

@Document(collection = "videos")
public class Video extends Media {

    public static final String ID = "id";
    public static final String MODEL_RUN_DT = "model_run_dt";
    public static final String PHENOMENON = "phenomenon";

    @Id
    private String id;
    private DateTime modelRunDT;
    private Phenomenon phenomenon;

    public Video() {
    }

    @PersistenceConstructor
    public Video(DateTime modelRunDT, Phenomenon phenomenon, byte[] data) {
        super(data);
        this.modelRunDT = modelRunDT;
        this.phenomenon = phenomenon;
    }

    public String getId() {
        return id;
    }

    public DateTime getModelRunDT() {
        return modelRunDT;
    }

    public Phenomenon getPhenomenon() {
        return phenomenon;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", modelRunDT=" + modelRunDT +
                ", phenomenon=" + phenomenon +
                "} " + super.toString();
    }

}
