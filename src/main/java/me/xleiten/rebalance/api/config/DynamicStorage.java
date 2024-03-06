package me.xleiten.rebalance.api.config;

import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.function.Consumer;

public final class DynamicStorage
{
    public final String name;

    final Section root = new Section(this);
    final DynamicStorageLoader loader;
    private boolean strictMode = false;
    Logger logger;

    private DynamicStorage(String name, Path fileDir) {
        this.name = name;
        this.logger = LoggerFactory.getLogger("Config(" + name + ")");
        this.loader = new DynamicStorageLoader(this, fileDir);
    }

    public static DynamicStorage create(@NotNull String name, @Nullable Path configDir) {
        return new DynamicStorage(name.isEmpty() ? "awesome-config" : name, configDir);
    }

    public static DynamicStorage create(@NotNull String name) {
        return create(name, Path.of("config"));
    }

    public DynamicStorage addTypeAdapter(@NotNull IType<?, ?> typeAdapter) {
        loader.addType(typeAdapter);
        return this;
    }

    public DynamicStorage useStrictMode() {
        this.strictMode = true;
        return this;
    }

    public DynamicStorage setLogger(@NotNull Logger logger) {
        this.logger = logger;
        return this;
    }

    public boolean isStrictMode() {
        return strictMode;
    }

    public DynamicStorage load(Consumer<ActionResult> callback) {
        loader.load(callback);
        return this;
    }

    public DynamicStorage load() {
        return load(null);
    }

    public void save(boolean clearUnused, Consumer<ActionResult> callback) {
        if (clearUnused) {
            this.root.clearUnusedEntries(true);
        }
        loader.save(callback);
    }

    public void save(boolean clearUnused) {
        save(clearUnused, null);
    }

    public <T> Option<T> option(@NotNull String path, @NotNull T defaultValue) {
        return root.option(path, defaultValue);
    }

    public Section section(@NotNull String path) {
        return root.section(path);
    }

    public Section getRoot() {
        return root;
    }
}
