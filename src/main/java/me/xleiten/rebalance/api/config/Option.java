package me.xleiten.rebalance.api.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public final class Option<T> extends DynamicStorageEntry
{
    public final String name;
    private T value;

    Option(@NotNull String name, T value, @NotNull DynamicStorage storage) {
        super(storage);
        this.name = name;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(@NotNull T value) {
        this.value =  value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void copyFrom(DynamicStorageEntry source) {
        if (source instanceof Option<?> option) {
            if (option.getValue().getClass() == value.getClass())
                this.setValue((T) option.getValue());
        } else
            config.logger.warn("Attempt to copy option value from non option config entry. Skipping...");
    }
}
