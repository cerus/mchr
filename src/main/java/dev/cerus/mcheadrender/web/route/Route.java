package dev.cerus.mcheadrender.web.route;

import com.google.gson.JsonObject;
import dev.cerus.mcheadrender.Globals;
import static dev.cerus.mcheadrender.Globals.LOGGER;
import dev.cerus.mcheadrender.web.WebServer;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import javax.imageio.ImageIO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an api route
 */
public abstract class Route implements Handler {

    protected final WebServer webServer;

    protected Route(final WebServer webServer) {
        this.webServer = webServer;
    }

    /**
     * Get the api version of this route
     * <p>
     * Has to be in the following format: 'v\d*' (eg v1, v2, v3, ...)
     *
     * @return The version
     */
    @NotNull
    public abstract String apiVersion();

    /**
     * Get the path
     * <p>
     * Has to start with a /
     *
     * @return The path
     */
    @NotNull
    public abstract String path();

    /**
     * Get a simple description of this route
     *
     * @return The description
     */
    @NotNull
    public abstract String description();

    /**
     * Get a collection of possible query parameters for this route
     *
     * @return A collection of query params
     */
    @Nullable
    public abstract Collection<QueryParam> params();

    /**
     * Get the route type
     *
     * @return The route type
     */
    @NotNull
    public abstract HandlerType type();

    /**
     * Respond with json to a request
     *
     * @param context The requesting context
     * @param o       The response
     */
    protected void json(@NotNull final Context context, @NotNull final JsonObject o) {
        context.status(200).result(o.toString());
        Globals.CONNECTIONS.decrementAndGet();
    }

    /**
     * Respond with an image to a request
     *
     * @param context The requesting context
     * @param img     The response
     */
    protected void img(@NotNull final Context context, @NotNull final BufferedImage img) {
        final byte[] bytes;
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", out);
            bytes = out.toByteArray();
        } catch (final IOException e) {
            this.error(context, "Internal server error");
            return;
        }

        context.header("Content-Type", "image/png")
                .status(200)
                .result(bytes);
        Globals.CONNECTIONS.decrementAndGet();
        LOGGER.info(context.ip() + " @ /" + this.apiVersion() + this.path() + ": Image sent");
    }

    /**
     * Respond with an error to a request
     *
     * @param context The requesting context
     * @param err     The response
     */
    protected void error(@NotNull final Context context, @NotNull final String err) {
        context.header("Content-Type", "application/json")
                .status(400)
                .result("{\"error\": \"%s\"}".formatted(err));
        Globals.CONNECTIONS.decrementAndGet();
        LOGGER.info(context.ip() + " @ /" + this.apiVersion() + this.path() + ": Error: " + err);
    }

    /**
     * Get a query parameter from a request
     *
     * @param ctx The request
     * @param key The param name
     *
     * @return The value or an empty string
     */
    @Nullable
    protected String queryParam(@NotNull final Context ctx, @NotNull final String key) {
        return this.queryParam(ctx, key, "");
    }

    /**
     * Get a query parameter from a request or a default value if it's not set
     *
     * @param ctx The request
     * @param key The param name
     * @param def The default value
     *
     * @return The value or an empty string
     */
    @Nullable
    protected String queryParam(@NotNull final Context ctx, @NotNull final String key, @Nullable final String def) {
        final String param = ctx.queryParam(key);
        return param == null ? def : param;
    }

}
