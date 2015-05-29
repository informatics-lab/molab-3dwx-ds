package uk.co.informaticslab.molab3dwxds.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * POJO for an image
 */
@Document(collection = "images")
public class Image extends Media {

    public static final String ID = "id";
    public static final String MODEL_RUN_DT = "model_run_dt";
    public static final String FORECAST_DT = "forecast_dt";
    public static final String PHENOMENON = "phenomenon";
    public static final String DATA_DIMENSIONS = "data_dimensions";

    @Id
    private String id;
    private DateTime modelRunDT;
    private DateTime forecastDT;
    private Phenomenon phenomenon;
    private DataDimensions dataDimensions;

    public Image() {
    }

    @PersistenceConstructor
    public Image(byte[] data, String mimeType, Resolution resolution, DateTime modelRunDT, DateTime forecastDT, Phenomenon phenomenon, DataDimensions dataDimensions) {
        super(data, mimeType, resolution);
        this.modelRunDT = modelRunDT;
        this.forecastDT = forecastDT;
        this.phenomenon = phenomenon;
        this.dataDimensions = dataDimensions;
    }

    public String getId() {
        return id;
    }

    public DateTime getModelRunDT() {
        return modelRunDT;
    }

    public DateTime getForecastDT() {
        return forecastDT;
    }

    public Phenomenon getPhenomenon() {
        return phenomenon;
    }

    public DataDimensions getDataDimensions() {
        return dataDimensions;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", modelRunDT=" + modelRunDT +
                ", forecastDT=" + forecastDT +
                ", phenomenon=" + phenomenon +
                ", dataDimensions=" + dataDimensions +
                "} " + super.toString();
    }
}
