package uk.co.informaticslab.molab3dwxds.api.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class DefaultExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionMapper.class);

    @Override
    public Response toResponse(Exception e) {

        LOGGER.error("An error occurred", e);

        return Response.serverError().entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build();
    }
}
