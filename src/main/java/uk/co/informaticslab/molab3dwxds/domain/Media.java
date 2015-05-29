package uk.co.informaticslab.molab3dwxds.domain;

import java.util.Arrays;

/**
 * POJO for a media entity
 */
public abstract class Media {

    public static final String DATA = "data";
    public static final String RESOLUTION = "resolution";
    public static final String MIME_TYPE = "mime_type";

    private byte[] data;
    private String mimeType;
    private Resolution resolution;

    public Media() {
    }

    public Media(byte[] data, String mimeType, Resolution resolution) {
        this.data = data;
        this.mimeType = mimeType;
        this.resolution = resolution;
    }

    public byte[] getData() {
        return data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public int getLength() {
        return data.length;
    }

    @Override
    public String toString() {
        return "Media{" +
                "data=" + Arrays.toString(data) +
                ", mimeType='" + mimeType + '\'' +
                ", resolution=" + resolution +
                '}';
    }

}
