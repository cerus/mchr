package dev.cerus.mcheadrender;

import dev.cerus.mcheadrender.config.JsonFileSkinProviderConfig;
import dev.cerus.mcheadrender.config.SkinProviderConfig;
import dev.cerus.mcheadrender.filter.Filter;
import dev.cerus.mcheadrender.filter.GrayscaleFilter;
import dev.cerus.mcheadrender.filter.InvertFilter;
import dev.cerus.mcheadrender.image.DefaultImageCache;
import dev.cerus.mcheadrender.renderer.FlatRenderer;
import dev.cerus.mcheadrender.renderer.IsometricRenderer;
import dev.cerus.mcheadrender.renderer.Renderer;
import dev.cerus.mcheadrender.skin.MojangSkinProvider;
import dev.cerus.mcheadrender.skin.SkinProvider;
import dev.cerus.mcheadrender.util.Registry;
import dev.cerus.mcheadrender.web.WebServer;
import java.io.File;
import java.io.IOException;

/**
 * Program entry point
 */
public class Launcher {

    public static void main(final String[] args) throws IOException {
        // Init registries
        final Registry<Renderer> rendererRegistry = new Registry<>(Renderer::id, "flat", new FlatRenderer(), new IsometricRenderer());
        final Registry<SkinProvider> skinProviderRegistry = new Registry<>(SkinProvider::id, "mojang", new MojangSkinProvider());
        final Registry<Filter> filterRegistry = new Registry<>(Filter::id, null, new GrayscaleFilter(), new InvertFilter());

        // Init skin provider config
        final SkinProviderConfig skinProviderConfig = new JsonFileSkinProviderConfig(new File("skin_provider.json"));
        skinProviderConfig.loadConfig(skinProviderRegistry);

        // Init and start web server
        final WebServer webServer = new WebServer(
                rendererRegistry,
                skinProviderRegistry,
                filterRegistry,
                new DefaultImageCache()
        );
        webServer.start(Globals.API_HOST, Globals.API_PORT);

        // Shutdown logic
        Runtime.getRuntime().addShutdownHook(new Thread(webServer::stop));
    }

}
