package dev.cerus.mcheadrender.web.route.v1;

import dev.cerus.mcheadrender.Globals;
import static dev.cerus.mcheadrender.Globals.LOGGER;
import dev.cerus.mcheadrender.image.ImageCache;
import dev.cerus.mcheadrender.renderer.Renderer;
import dev.cerus.mcheadrender.skin.SkinProvider;
import dev.cerus.mcheadrender.web.WebServer;
import dev.cerus.mcheadrender.web.route.QueryParam;
import dev.cerus.mcheadrender.web.route.Route;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import javax.imageio.ImageIO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * /v1/render
 * <p>
 * Renders a skin
 */
public class RenderRouteV1 extends Route {

    protected RenderRouteV1(final WebServer webServer) {
        super(webServer);
    }

    @Override
    public void handle(@NotNull final Context ctx) throws Exception {
        Globals.CONNECTIONS.incrementAndGet();
        LOGGER.info(ctx.ip() + " @ /v1/render: " + ctx.fullUrl());

        if (this.webServer.getRateLimiter().isRateLimited(ctx)) {
            this.error(ctx, "Too many requests");
            return;
        }

        final SkinProvider skinProvider = this.webServer.getSkinProviderRegistry().getOrFallback(this.queryParam(ctx, "skin"));
        final Renderer renderer = this.webServer.getRendererRegistry().getOrFallback(this.queryParam(ctx, "renderer"));
        final int size = Math.max(Globals.MIN_IMAGE_SIZE, Math.min(Globals.MAX_IMAGE_SIZE, Integer.parseInt(this.queryParam(ctx, "size", "8"))));
        final boolean overlay = Boolean.parseBoolean(this.queryParam(ctx, "overlay", "true"));
        final String key = ctx.pathParam("key");

        if (!skinProvider.acceptsKey(key)) {
            this.error(ctx, "Invalid skin key (" + skinProvider.id() + ")");
            return;
        }

        final BufferedImage image;
        final String skinUrl = skinProvider.getUrl(key);
        final ImageCache imageCache = this.webServer.getImageCache();
        if (imageCache.isCached(skinUrl)) {
            image = imageCache.getCached(skinUrl);
            ctx.header("X-Cached-Expiration", String.valueOf(imageCache.getExpiry(skinUrl)));
        } else {
            try {
                final HttpURLConnection connection = (HttpURLConnection) new URL(skinUrl).openConnection();
                connection.setRequestProperty("User-Agent", "github/cerus/mc-head-render");
                connection.setDoInput(true);

                InputStream in;
                try {
                    in = connection.getInputStream();
                } catch (final IOException ignored) {
                    in = connection.getErrorStream();
                }

                image = ImageIO.read(in);
                connection.disconnect();

                imageCache.cache(skinUrl, image);
            } catch (final IOException e) {
                this.error(ctx, "Internal server error");
                return;
            }
        }

        if (image == null) {
            this.error(ctx, "Skin not found");
            return;
        }

        final BufferedImage renderedHead = renderer.render(image, size, overlay);
        this.img(ctx, renderedHead);
    }

    @Override
    public @NotNull String apiVersion() {
        return "v1";
    }

    @Override
    public @NotNull String path() {
        return "/render/{key}";
    }

    @Override
    public @NotNull String description() {
        return "Render a head";
    }

    @Override
    public @Nullable Collection<QueryParam> params() {
        return List.of(
                new QueryParam("skin", "The skin provider", String.class, true,
                        List.copyOf(this.webServer.getSkinProviderRegistry().ids()), null, "mojang"),
                new QueryParam("renderer", "The renderer", String.class, true,
                        List.of("flat", "isometric"), null, "flat"),
                new QueryParam("size", "The desired width and height", int.class, true,
                        null, List.of("128", "512", "16"), "8"),
                new QueryParam("overlay", "Whether the hat overlay should be rendered or not",
                        boolean.class, true, List.of("true", "false"), null, "true")
        );
    }

    @Override
    public @NotNull HandlerType type() {
        return HandlerType.GET;
    }

}
