package dev.cerus.mcheadrender.web;

import dev.cerus.mcheadrender.Globals;
import io.javalin.http.Context;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.jetbrains.annotations.NotNull;

/**
 * A simple rate limiter
 */
public class RateLimiter {

    private final Map<String, Integer> requestMap = new ConcurrentHashMap<>();
    private final ExpiringMap<String, Long> rateLimitMap = ExpiringMap.builder()
            .expiration(Globals.RATE_LIMITER_EXPIRATION, TimeUnit.SECONDS)
            .expirationPolicy(ExpirationPolicy.CREATED)
            .expirationListener((key, value) -> this.requestMap.remove(key))
            .build();

    /**
     * Check if an ip is rate limited
     * <p>
     * Will also increment the amount of requests this ip has made
     *
     * @param ctx The request
     *
     * @return True if the ip is rate limited, false if not
     */
    public boolean isRateLimited(@NotNull final Context ctx) {
        this.requestMap.merge(ctx.ip(), 1, Integer::sum);
        if (!this.rateLimitMap.containsKey(ctx.ip())) {
            this.rateLimitMap.put(ctx.ip(), System.currentTimeMillis());
        }
        return this.requestMap.getOrDefault(ctx.ip(), 0) >= Globals.MAX_REQUESTS;
    }

    /**
     * Get the amount of requests an ip has done
     *
     * @param ctx The request
     *
     * @return The amount of requests
     */
    public int getRequests(@NotNull final Context ctx) {
        return this.requestMap.getOrDefault(ctx.ip(), 0);
    }

}
