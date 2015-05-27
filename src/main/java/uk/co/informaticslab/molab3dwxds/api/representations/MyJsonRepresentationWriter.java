package uk.co.informaticslab.molab3dwxds.api.representations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import com.theoryinpractise.halbuilder.json.JsonRepresentationWriter;

import java.net.URI;
import java.util.Set;

/**
 * Overrides the getJsonFactory method on the superclass to allow configuration of the ObjectMapper.
 */
public class MyJsonRepresentationWriter extends JsonRepresentationWriter {

    @Override
    protected JsonFactory getJsonFactory(Set<URI> flags) {
        JsonFactory f = new JsonFactory();
        ObjectMapper codec = new ObjectMapper();
        if (flags.contains(RepresentationFactory.STRIP_NULLS)) {
            codec.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        codec.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, false);
        codec.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        codec.registerModule(new JodaModule());
        f.setCodec(codec);
        f.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        return f;
    }

}
