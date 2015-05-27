package uk.co.informaticslab.molab3dwxds.api.caching.utils;

import javax.ws.rs.core.CacheControl;

/**
 * Utility class used for constructing predefined Cache-Control directives.
 * <p>
 * The cache controls created by this class take a "belt and braces" approach due to the current
 * inconsistencies with proxies and user agent caching behaviour. For more information see the link
 * below:
 * </p>
 * <a href="http://www.mobify.com/blog/beginners-guide-to-http-cache-headers/">beginners-guide-to-
 * http-cache-headers</a>
 */
public final class CacheControlUtils {

    private static final int EXPIRES_IMMEDIATELY = 0;
    private static final int MINUTE = 60;
    private static final int HOUR = MINUTE * 60;
    private static final int DAY = HOUR * 24;
    private static final int YEAR = DAY * 365;

    /**
     * <p>
     * Cache-Control: private, no-cache, no-store, no-transform, must-revalidate, max-age=0
     * </p>
     * Rational: <b>private</b> should stop proxies from caching. <b>no-store</b> should stop
     * everything from caching. <b>no-cache</b>, <b>must-revalidate</b> and <b>max-age=0</b> should
     * force a user agent to revalidate a resource
     */
    public static CacheControl neverCache() {

        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoTransform(true);
        cacheControl.setPrivate(true);
        cacheControl.setNoStore(true);
        cacheControl.setNoCache(true);
        cacheControl.setMustRevalidate(true);
        cacheControl.setProxyRevalidate(false);
        cacheControl.setMaxAge(EXPIRES_IMMEDIATELY);

        return cacheControl;
    }

    /**
     * <p>
     * Cache-Control: no-cache, no-transform, must-revalidate, proxy-revalidate, max-age=0,
     * s-maxage=0
     * </p>
     * Rational: <b>no-cache</b>, <b>must-revalidate</b> and <b>max-age=0</b> should force a user
     * agent to revalidate a resource. <b>no-cache</b>, <b>proxy-revalidate</b> and
     * <b>s-maxage=0</b> should force intermediary caches to revalidate a resource.
     */
    public static CacheControl conditionallyCache() {

        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoTransform(true);
        cacheControl.setPrivate(false);
        cacheControl.setNoStore(false);
        cacheControl.setNoCache(true);
        cacheControl.setMustRevalidate(true);
        cacheControl.setProxyRevalidate(true);
        cacheControl.setMaxAge(EXPIRES_IMMEDIATELY);
        cacheControl.setSMaxAge(EXPIRES_IMMEDIATELY);

        return cacheControl;
    }

    /**
     * <p>
     * Cache-Control: no-transform, max-age=3600, s-maxage=86400
     * </p>
     * Rational: <b>max-age=31536000</b> specifies that user agents may cache a resource for an
     * hour. <b>s-maxage=86400</b> specifies that intermediary caches may cache a resource for a
     * day. These values are a reasonable trade-off for cacheable resources without the pitfalls of
     * {@link #permanent()}
     */
    public static CacheControl cache() {

        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoTransform(true);
        cacheControl.setPrivate(false);
        cacheControl.setNoStore(false);
        cacheControl.setNoCache(false);
        cacheControl.setMustRevalidate(false);
        cacheControl.setProxyRevalidate(false);
        cacheControl.setMaxAge(HOUR);
        cacheControl.setSMaxAge(DAY);

        return cacheControl;
    }

    /**
     * <p>
     * Cache-Control: no-transform, max-age=31536000, s-maxage=31536000
     * </p>
     * Rational: <b>max-age=31536000</b> and <b>s-maxage=31536000</b> specifies that user agents and
     * intermediary caches may cache a resource for the maximum time duration permitted, one year.
     * Note: this directive should only be used provided it is an absolute certainty that the
     * associated resource will not change, else use {@link #cache()}
     */
    public static CacheControl permanent() {

        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoTransform(true);
        cacheControl.setPrivate(false);
        cacheControl.setNoStore(false);
        cacheControl.setNoCache(false);
        cacheControl.setMustRevalidate(false);
        cacheControl.setProxyRevalidate(false);
        cacheControl.setMaxAge(YEAR);
        cacheControl.setSMaxAge(YEAR);

        return cacheControl;
    }

    private CacheControlUtils() {
        // Utility class
    }
}
