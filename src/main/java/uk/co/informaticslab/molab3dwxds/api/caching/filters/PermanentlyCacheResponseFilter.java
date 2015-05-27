package uk.co.informaticslab.molab3dwxds.api.caching.filters;


import uk.co.informaticslab.molab3dwxds.api.caching.bindings.PermanentlyCache;
import uk.co.informaticslab.molab3dwxds.api.caching.utils.CacheControlUtils;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.Provider;

@Priority(Priorities.USER)
@PermanentlyCache
@Provider
public class PermanentlyCacheResponseFilter extends CacheabilityResponseFilter {

    private static final String PERMANENTLY_CACHE = CacheControlUtils.permanent().toString();

    @Override
    protected String getCacheControlHeader() {
        return PERMANENTLY_CACHE;
    }
}
