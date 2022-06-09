package dev.cerus.mcheadrender.web.route.v1;

import dev.cerus.mcheadrender.Globals;
import static dev.cerus.mcheadrender.Globals.LOGGER;
import dev.cerus.mcheadrender.web.WebServer;
import dev.cerus.mcheadrender.web.route.QueryParam;
import dev.cerus.mcheadrender.web.route.Route;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * /v1/info
 * <p>
 * Shows info such as ratelimit status, cached skins and more
 */
public class InfoRouteV1 extends Route {

    public InfoRouteV1(final WebServer webServer) {
        super(webServer);
    }

    @Override
    public void handle(@NotNull final Context ctx) throws Exception {
        Globals.CONNECTIONS.incrementAndGet();
        LOGGER.info(ctx.ip() + " @ /v1/info: " + ctx.fullUrl());

        if (this.webServer.getRateLimiter().isRateLimited(ctx)) {
            this.error(ctx, "Too many requests");
            return;
        }

        ctx.status(200).result("""
                {
                  "connections": %d,
                  "cache": {
                    "cached": %d,
                    "expiration_in_seconds": %d
                  },
                  "skin_provider": %s,
                  "renderer": %s,
                  "ratelimit": {
                    "max_requests": %d,
                    "period_in_seconds": %d,
                    "your_requests": %d
                  }
                }
                """.formatted(
                Globals.CONNECTIONS.get(),
                this.webServer.getImageCache().cached(),
                Globals.IMAGE_CACHE_EXPIRATION,
                "[" + this.webServer.getSkinProviderRegistry().ids().stream()
                        .map(s -> "\"" + s + "\"")
                        .collect(Collectors.joining(", ")) + "]",
                "[" + this.webServer.getRendererRegistry().ids().stream()
                        .map(s -> "\"" + s + "\"")
                        .collect(Collectors.joining(", ")) + "]",
                Globals.MAX_REQUESTS,
                Globals.RATE_LIMITER_EXPIRATION,
                this.webServer.getRateLimiter().getRequests(ctx)
        ));
        Globals.CONNECTIONS.decrementAndGet();
    }

    @Override
    public @NotNull String apiVersion() {
        return "v1";
    }

    @Override
    public @NotNull String path() {
        return "/info";
    }

    @Override
    public @NotNull String description() {
        return "Get information about this MCHR instance";
    }

    @Override
    public @Nullable Collection<QueryParam> params() {
        return Set.of();
    }

    @Override
    public @NotNull HandlerType type() {
        return HandlerType.GET;
    }

}
