package dev.cerus.mcheadrender.image;

import java.awt.image.BufferedImage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A cache for retrieved skins
 */
public abstract class ImageCache {

    /**
     * Cache a skin
     *
     * @param url The skin's download url
     * @param img The skin image
     */
    public abstract void cache(@NotNull String url, @NotNull BufferedImage img);

    /**
     * Check if a skin url is cached
     *
     * @param url The skin url
     *
     * @return True if it is cached, false if not
     */
    public abstract boolean isCached(@NotNull String url);

    /**
     * Get the cached skin image of a url
     *
     * @param url The skin url
     *
     * @return The cached skin image
     */
    @Nullable
    public abstract BufferedImage getCached(@NotNull String url);

    /**
     * Get the expiration timestamp of a cached skin url
     *
     * @param url The skin url
     *
     * @return The expiration timestamp
     */
    public abstract long getExpiry(@NotNull String url);

    /**
     * Get the amount of cached skins
     *
     * @return The amount of cached skins
     */
    public abstract int cached();

}
