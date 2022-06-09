package dev.cerus.mcheadrender.skin;

import org.jetbrains.annotations.NotNull;

/**
 * A configurable skin provider
 */
public class SimpleSkinProvider extends SkinProvider {

    private final String id;
    private final String urlTemplate;
    private final String keyPattern;

    public SimpleSkinProvider(final String id, final String urlTemplate, final String keyPattern) {
        this.id = id;
        this.urlTemplate = urlTemplate;
        this.keyPattern = keyPattern;
    }

    @Override
    public @NotNull String getUrlTemplate() {
        return this.urlTemplate;
    }

    @Override
    public @NotNull String pattern() {
        return this.keyPattern;
    }

    @Override
    public @NotNull String id() {
        return this.id;
    }

}
