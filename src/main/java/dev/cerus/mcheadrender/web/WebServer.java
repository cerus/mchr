package dev.cerus.mcheadrender.web;

import dev.cerus.mcheadrender.filter.Filter;
import dev.cerus.mcheadrender.image.ImageCache;
import dev.cerus.mcheadrender.renderer.Renderer;
import dev.cerus.mcheadrender.skin.SkinProvider;
import dev.cerus.mcheadrender.util.Registry;
import dev.cerus.mcheadrender.web.route.RouteDocumenter;
import dev.cerus.mcheadrender.web.route.v1.RoutesV1;
import io.javalin.Javalin;

/**
 * A simple Javalin web server
 */
public class WebServer {

    private final RateLimiter rateLimiter = new RateLimiter();
    private final Registry<Renderer> rendererRegistry;
    private final Registry<SkinProvider> skinProviderRegistry;
    private final ImageCache imageCache;
    private final Registry<Filter> filterRegistry;
    private Javalin app;

    public WebServer(final Registry<Renderer> rendererRegistry,
                     final Registry<SkinProvider> skinProviderRegistry,
                     final Registry<Filter> filterRegistry,
                     final ImageCache imageCache) {
        this.rendererRegistry = rendererRegistry;
        this.skinProviderRegistry = skinProviderRegistry;
        this.filterRegistry = filterRegistry;
        this.imageCache = imageCache;
    }

    /**
     * Start the web server
     *
     * @param host The server host
     * @param port The server port
     */
    public void start(final String host, final int port) {
        this.app = Javalin.create();
        RoutesV1.register(this);
        RouteDocumenter.register(this);
        this.app.exception(Exception.class, (e, ctx) -> ctx.status(500));
        this.app.start(host, port);
    }

    /**
     * Stop the web server
     */
    public void stop() {
        this.app.close();
    }

    public Javalin getApp() {
        return this.app;
    }

    public RateLimiter getRateLimiter() {
        return this.rateLimiter;
    }

    public ImageCache getImageCache() {
        return this.imageCache;
    }

    public Registry<Renderer> getRendererRegistry() {
        return this.rendererRegistry;
    }

    public Registry<SkinProvider> getSkinProviderRegistry() {
        return this.skinProviderRegistry;
    }

    public Registry<Filter> getFilterRegistry() {
        return this.filterRegistry;
    }

}
