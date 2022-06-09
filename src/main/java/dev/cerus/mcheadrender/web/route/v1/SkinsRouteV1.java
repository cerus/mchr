package dev.cerus.mcheadrender.web.route.v1;

import com.google.gson.JsonObject;
import dev.cerus.mcheadrender.Globals;
import static dev.cerus.mcheadrender.Globals.LOGGER;
import dev.cerus.mcheadrender.skin.SkinProvider;
import dev.cerus.mcheadrender.web.WebServer;
import dev.cerus.mcheadrender.web.route.QueryParam;
import dev.cerus.mcheadrender.web.route.Route;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * /v1/skins
 * <p>
 * Responds with a list of registered skin providers
 */
public class SkinsRouteV1 extends Route {

    protected SkinsRouteV1(final WebServer webServer) {
        super(webServer);
    }

    @Override
    public void handle(@NotNull final Context ctx) throws Exception {
        Globals.CONNECTIONS.incrementAndGet();
        LOGGER.info(ctx.ip() + " @ /v1/skins: " + ctx.fullUrl());

        if (this.webServer.getRateLimiter().isRateLimited(ctx)) {
            this.error(ctx, "Too many requests");
            return;
        }

        final JsonObject root = new JsonObject();
        for (final String id : this.webServer.getSkinProviderRegistry().ids()) {
            final SkinProvider skinProvider = this.webServer.getSkinProviderRegistry().get(id);
            final JsonObject entry = new JsonObject();
            entry.addProperty("url", skinProvider.getUrlTemplate());
            entry.addProperty("key_pattern", skinProvider.pattern());
            root.add(skinProvider.id(), entry);
        }
        this.json(ctx, root);
    }

    @Override
    public @NotNull String apiVersion() {
        return "v1";
    }

    @Override
    public @NotNull String path() {
        return "/skins";
    }

    @Override
    public @NotNull String description() {
        return "List all available skin providers";
    }

    @Override
    public @Nullable Collection<QueryParam> params() {
        return null;
    }

    @Override
    public @NotNull HandlerType type() {
        return HandlerType.GET;
    }

}
