package dev.cerus.mcheadrender.skin;

import org.jetbrains.annotations.NotNull;

/**
 * Provides skin url's to retrieve skins
 */
public abstract class SkinProvider {

    /**
     * Get the template
     * <p>
     * The template must include a '%s' to insert the skin key
     * <p>
     * Example: https://some-minecraft-skin.com/%s.png
     *
     * @return The url template
     */
    @NotNull
    public abstract String getUrlTemplate();

    /**
     * Generate an url based on the template and key
     *
     * @param key The skin key
     *
     * @return The skin url
     */
    @NotNull
    public String getUrl(@NotNull final String key) {
        return this.getUrlTemplate().formatted(key);
    }

    /**
     * Check if this provider accepts a skin key
     *
     * @param key The skin key
     *
     * @return True if this provider accepts the key, false if not
     */
    public boolean acceptsKey(@NotNull final String key) {
        return key.matches(this.pattern());
    }

    /**
     * Get the key pattern that potential keys are matched against
     *
     * @return The skin key pattern
     */
    @NotNull
    public abstract String pattern();

    /**
     * Get the id of this skin provider
     *
     * @return The id
     */
    @NotNull
    public abstract String id();

}
