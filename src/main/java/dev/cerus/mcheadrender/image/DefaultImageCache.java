package dev.cerus.mcheadrender.image;

import dev.cerus.mcheadrender.Globals;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;
import net.jodah.expiringmap.ExpiringMap;
import org.jetbrains.annotations.NotNull;

/**
 * Simple image cache implementation using an expiring map
 */
public class DefaultImageCache extends ImageCache {

    private final ExpiringMap<String, BufferedImage> imageMap = ExpiringMap.builder()
            .expiration(Globals.IMAGE_CACHE_EXPIRATION, TimeUnit.SECONDS)
            .build();

    @Override
    public void cache(final @NotNull String url, final @NotNull BufferedImage img) {
        this.imageMap.put(url, img);
    }

    @Override
    public boolean isCached(final @NotNull String url) {
        return this.imageMap.containsKey(url);
    }

    @Override
    public BufferedImage getCached(final @NotNull String url) {
        return this.imageMap.get(url);
    }

    @Override
    public long getExpiry(final @NotNull String url) {
        return this.imageMap.getExpiration(url) + System.currentTimeMillis();
    }

    @Override
    public int cached() {
        return this.imageMap.size();
    }
}