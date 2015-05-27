package uk.co.informaticslab.molab3dwxds.api.representations;

import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.json.JsonRepresentationReader;

/**
 * Custom representation factory for JSON allows us to inject our own renderer/reader.
 */
public class MyRepresentationFactory extends DefaultRepresentationFactory {
    public MyRepresentationFactory() {
        withRenderer(HAL_JSON, MyJsonRepresentationWriter.class);
        withReader(HAL_JSON, JsonRepresentationReader.class);
    }
}
