package uk.co.informaticslab.molab3dwxds.domain;

import java.util.Arrays;

/**
 * POJO for a media entity
 */
public abstract class Media {

    public static final String DATA = "data";

    private byte[] data;


    public Media() {
    }

    public Media(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Media{" +
                "data=" + Arrays.toString(data) +
                '}';
    }

    public int getLength() {
        return data.length;
    }
}
