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

    //TODO replace exceptions thrown here with webapplication exceptions

    private final FormDataMultiPart form;

    public MediaForm(FormDataMultiPart form) {
        this.form = form;
    }

    public String getAsString(String fieldName) {
        FormDataBodyPart field = form.getField(fieldName);
        if (field != null) {
            return field.getValue();
        } else {
            throw new IllegalArgumentException("Expected a String value to be present for " + fieldName);
        }
    }

    public Integer getAsInteger(String fieldName) {
        FormDataBodyPart field = form.getField(fieldName);
        if (field != null) {
            return field.getValueAs(Integer.class);
        } else {
            throw new IllegalArgumentException("Expected an Integer value to be present for " + fieldName);
        }
    }

    public DateTime getAsDateTime(String fieldName) {
        try {
            return new DateTime(getAsString(fieldName));
        } catch (Exception e) {
            throw new IllegalArgumentException("Expected a DateTime value to be present for " + fieldName, e);
        }
    }

    public byte[] getAsData(String fieldName) {
        FormDataBodyPart field = form.getField(fieldName);
        if (field != null) {
            try {
                return ByteStreams.toByteArray(field.getValueAs(InputStream.class));
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not convert data to byte[]", e);
            }
        } else {
            throw new IllegalArgumentException("Expected a byte[] value to be present for " + fieldName);
        }
    }

    public List<GeographicPoint> getAsGeographicRegion(String fieldName) {
        try {
            String jsonString = getAsString(fieldName);
            ObjectMapper mapper = new ObjectMapper();
            List<GeographicPoint> geographicPointList = mapper.readValue(jsonString,
                    TypeFactory.defaultInstance().constructCollectionType(List.class,
                            GeographicPoint.class));
            if (geographicPointList.size() != 4) {
                throw new IllegalArgumentException("A geographic region must consist of 4 geographic points");
            }
            return geographicPointList;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to create geographic region", e);
        }

    }

    public Resolution getAsResolution(String xFieldName, String yFieldName) {
        try {
            Integer xValue = getAsInteger(xFieldName);
            Integer yValue = getAsInteger(yFieldName);
            return new Resolution(xValue, yValue);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to determine resolution", e);
        }
    }

    public DataDimensions getAsDataDimensions(String xFieldName, String yFieldName, String zFieldName) {
        try {
            Integer xValue = getAsInteger(xFieldName);
            Integer yValue = getAsInteger(yFieldName);
            Integer zValue = getAsInteger(zFieldName);
            return new DataDimensions(xValue, yValue, zValue);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to determine data dimensions", e);
        }
    }

}
