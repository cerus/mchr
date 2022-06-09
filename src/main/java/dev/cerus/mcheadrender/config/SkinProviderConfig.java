package dev.cerus.mcheadrender.config;

import dev.cerus.mcheadrender.skin.SkinProvider;
import dev.cerus.mcheadrender.util.Registry;
import org.jetbrains.annotations.NotNull;

/**
 * A configuration that's responsible for loading and registering configured skin providers
 */
public abstract class SkinProviderConfig {

    /**
     * Load and register the skin providers
     * <p>
     * Generates a default config if it doesn't already exist
     *
     * @param skinProviderRegistry The registry for skin providers
     */
    public void loadConfig(@NotNull final Registry<SkinProvider> skinProviderRegistry) {
        if (!this.exists()) {
            this.generateDefault();
        }
        this.load(skinProviderRegistry);
    }

    /**
     * Actually load and register skin providers
     *
     * @param skinProviderRegistry THe registry for skin providers
     */
    public abstract void load(@NotNull Registry<SkinProvider> skinProviderRegistry);

    /**
     * Checks if this config exists
     *
     * @return True if this config exists, false if not
     */
    public abstract boolean exists();

    /**
     * Generates a default config
     */
    public abstract void generateDefault();

}
