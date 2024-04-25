package me.xleiten.rebalance.api.config;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public final class StorageManager
{
    private final BiMap<String, DynamicStorage> storageMap = HashBiMap.create();

    public void addStorage(@NotNull DynamicStorage storage) {
        this.storageMap.put(storage.name, storage);
    }

    public Optional<DynamicStorage> getStorage(@NotNull String name) {
        return Optional.ofNullable(storageMap.get(name));
    }

    public void forEach(@NotNull Consumer<DynamicStorage> storageConsumer) {
        storageMap.forEach((s, storage) -> storageConsumer.accept(storage));
    }

    public void saveAll() {
        forEach(DynamicStorage::save);
    }

    public void loadAll() {
        forEach(DynamicStorage::load);
    }

}
