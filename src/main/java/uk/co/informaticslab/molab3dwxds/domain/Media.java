package uk.co.informaticslab.molab3dwxds.domain;

import java.util.Arrays;

/**
 * POJO for a media entity
 */
public abstract class Media {

    public static final String DATA = "data";
    public static final String RESOLUTION = "resolution";

    private byte[] data;
    private Resolution resolution;

    public Media() {

    }

    public Media(byte[] data, Resolution resolution) {
        this.data = data;
        this.resolution = resolution;
    }

    public byte[] getData() {
        return data;
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
                ", resolution=" + resolution +
                '}';
    }

}
