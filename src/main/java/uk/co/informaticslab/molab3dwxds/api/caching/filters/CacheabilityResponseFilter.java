package uk.co.informaticslab.molab3dwxds.api.caching.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.EnumSet;

/**
 * Base cache response filter used for setting Cache-Control directives on responses. If a
 * Cache-Control header has all ready been set or the response status is not cacheable this filter
 * has not effect. Sub classes need only provide the Cache-Control header to be used.
 */
public abstract class CacheabilityResponseFilter implements ContainerResponseFilter {

    private static final EnumSet<Status> CACHEABLE_STATUSES = EnumSet.of(Status.OK, Status.PARTIAL_CONTENT);
    private static final int NOT_SET = -1;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        if (!hasCacheControl(responseContext)) {

            final Status status = getStatus(responseContext);
            if (CACHEABLE_STATUSES.contains(status)) {

                verify(responseContext);

                final String cacheControl = getCacheControlHeader();
                responseContext.getHeaders().putSingle(HttpHeaders.CACHE_CONTROL, cacheControl);
            }
        }
    }

    protected abstract String getCacheControlHeader();

    protected void verify(ContainerResponseContext responseContext) {
        // no-op
    }

    private static boolean hasCacheControl(ContainerResponseContext responseContext) {
        return responseContext.getHeaders().containsKey(HttpHeaders.CACHE_CONTROL);
    }

    private static Status getStatus(ContainerResponseContext responseContext) {

        final int statusCode = responseContext.getStatus();
        if (statusCode == NOT_SET) {
            throw new FilterException("No status was set for the response");
        }
        return Status.fromStatusCode(statusCode);
    }
}
