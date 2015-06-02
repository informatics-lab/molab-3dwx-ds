package uk.co.informaticslab.molab3dwxds.domain;

import org.springframework.data.annotation.Id;

import java.util.Arrays;

/**
 * POJO for a media entity
 */
public abstract class Media {

    @Id
    private String id;

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

    public String getId() {
        return id;
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
                "id='" + id + '\'' +
                ", data=" + Arrays.toString(data) +
                ", mimeType='" + mimeType + '\'' +
                ", resolution=" + resolution +
                '}';
    }

}
