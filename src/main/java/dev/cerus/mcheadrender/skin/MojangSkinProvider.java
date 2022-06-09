package dev.cerus.mcheadrender.skin;

import org.jetbrains.annotations.NotNull;

/**
 * A skin provider for Mojang's texture servers
 * <p>
 * This is the only default provider
 */
public class MojangSkinProvider extends SkinProvider {

    @Override
    public @NotNull String getUrlTemplate() {
        return "http://textures.minecraft.net/texture/%s";
    }

    @Override
    public @NotNull String pattern() {
        return "[\\da-f]{64}";
    }

    @Override
    public @NotNull String id() {
        return "mojang";
    }

}
