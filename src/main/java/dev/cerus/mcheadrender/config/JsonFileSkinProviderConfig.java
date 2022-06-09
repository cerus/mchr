package dev.cerus.mcheadrender.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.LongSerializationPolicy;
import dev.cerus.mcheadrender.skin.SimpleSkinProvider;
import dev.cerus.mcheadrender.skin.SkinProvider;
import dev.cerus.mcheadrender.util.Registry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

/**
 * Skin provider config implementation for .json files
 */
public class JsonFileSkinProviderConfig extends SkinProviderConfig {

    private final File file;

    public JsonFileSkinProviderConfig(final File file) {
        this.file = file;
    }

    @Override
    public void load(final @NotNull Registry<SkinProvider> skinProviderRegistry) {
        final JsonObject root;
        try (final FileReader reader = new FileReader(this.file)) {
            root = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        final JsonObject skinProviderObj = root.getAsJsonObject("skin_provider");
        for (final String key : skinProviderObj.keySet()) {
            final JsonObject entry = skinProviderObj.get(key).getAsJsonObject();
            skinProviderRegistry.register(new SimpleSkinProvider(
                    key,
                    entry.get("url").getAsString(),
                    entry.get("pattern").getAsString()
            ));
        }
    }

    @Override
    public void generateDefault() {
        if (this.file.getParentFile() != null) {
            this.file.getParentFile().mkdirs();
        }

        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .create();

        final JsonObject root = new JsonObject();
        root.add("skin_provider", new JsonObject());

        try (final FileOutputStream out = new FileOutputStream(this.file)) {
            out.write(gson.toJson(root).getBytes(StandardCharsets.UTF_8));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists() {
        return this.file.exists();
    }

}
