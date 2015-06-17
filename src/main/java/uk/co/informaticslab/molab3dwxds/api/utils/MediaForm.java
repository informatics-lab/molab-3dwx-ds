package uk.co.informaticslab.molab3dwxds.api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.io.ByteStreams;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.joda.time.DateTime;
import uk.co.informaticslab.molab3dwxds.domain.DataDimensions;
import uk.co.informaticslab.molab3dwxds.domain.GeographicPoint;
import uk.co.informaticslab.molab3dwxds.domain.Resolution;

import java.io.InputStream;
import java.util.List;

/**
 * Created by tom on 17/06/2015.
 */
public class MediaForm {

    private final FormDataMultiPart form;

    public MediaForm(FormDataMultiPart form) {
        this.form = form;
    }

    public String getAsString(String fieldName) {
        String value = form.getField(fieldName).getValue();
        if (value != null) {
            return value;
        } else {
            throw new IllegalArgumentException("Expected a String value to be present for " + fieldName);
        }
    }

    public Integer getAsInteger(String fieldName) {
        Integer value = form.getField(fieldName).getValueAs(Integer.class);
        if (value != null) {
            return value;
        } else {
            throw new IllegalArgumentException("Expected an Integer value to be present for " + fieldName);
        }
    }

    public DateTime getAsDateTime(String fieldName) {
        String value = form.getField(fieldName).getValue();
        if (value != null) {
            return new DateTime(value);
        } else {
            throw new IllegalArgumentException("Expected a DateTime value to be present for " + fieldName);
        }
    }

    public byte[] getAsData(String fieldName) {
        FormDataBodyPart bodyPart = form.getField(fieldName);
        if (bodyPart != null) {
            try {
                return ByteStreams.toByteArray(bodyPart.getValueAs(InputStream.class));
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not convert data to byte[]", e);
            }
        } else {
            throw new IllegalArgumentException("Expected a byte[] value to be present for " + fieldName);
        }
    }

    public List<GeographicPoint> getAsGeographicRegion(String fieldName) {
        String jsonString = getAsString(fieldName);
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<GeographicPoint> geographicPointList = mapper.readValue(jsonString,
                    TypeFactory.defaultInstance().constructCollectionType(List.class,
                            GeographicPoint.class));
            if (geographicPointList.size() != 4) {
                throw new IllegalArgumentException("A geographic region must consist of 4 geographic points");
            }
            return geographicPointList;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to create geographic region from : " + jsonString, e);
        }

    }

    public Resolution getAsResolution(String xFieldName, String yFieldName) {
        Integer xValue = getAsInteger(xFieldName);
        Integer yValue = getAsInteger(yFieldName);
        return new Resolution(xValue, yValue);
    }

    public DataDimensions getAsDataDimensions(String xFieldName, String yFieldName, String zFieldName) {
        Integer xValue = getAsInteger(xFieldName);
        Integer yValue = getAsInteger(yFieldName);
        Integer zValue = getAsInteger(zFieldName);
        return new DataDimensions(xValue, yValue, zValue);
    }

}
