package dev.cerus.mcheadrender;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collection of constants
 */
public class Globals {

    public static final Logger LOGGER = LoggerFactory.getLogger("MCHR");

    public static final int IMAGE_CACHE_EXPIRATION = env("MCHR_IMAGE_CACHE_EXPIRATION", 5 * 60);
    public static final int MAX_REQUESTS = env("MCHR_MAX_REQUESTS", 120);
    public static final int RATE_LIMITER_EXPIRATION = env("MCHR_RATE_LIMITER_EXPIRATION", 1 * 60);
    public static final String API_HOST = env("MCHR_HOST", "localhost");
    public static final int API_PORT = env("MCHR_PORT", 8080);
    public static final AtomicInteger CONNECTIONS = new AtomicInteger(0);
    public static final int MAX_IMAGE_SIZE = 1000;
    public static final int MIN_IMAGE_SIZE = 8;

    private static long env(final String key, final long fallback) {
        return Long.parseLong(env(key, String.valueOf(fallback)));
    }

    private static int env(final String key, final int fallback) {
        return Integer.parseInt(env(key, String.valueOf(fallback)));
    }

    private static String env(final String key, final String fallback) {
        return Optional.ofNullable(System.getenv(key)).orElse(fallback);
    }

}
