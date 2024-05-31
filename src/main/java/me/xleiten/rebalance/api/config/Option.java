package me.xleiten.rebalance.api.config;

import org.jetbrains.annotations.NotNull;

public final class Option<T> extends DynamicStorageEntry
{
    public final String name;
    private T value;

    Option(@NotNull String name, T value, @NotNull DynamicStorage storage) {
        super(storage);
        this.name = name;
        this.value = value;
    }

    public T value() {
        return value;
    }

    public void value(@NotNull T value) {
        this.value =  value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void copyFrom(DynamicStorageEntry source) {
        if (source instanceof Option<?> option) {
            if (option.value().getClass() == value.getClass())
                this.value((T) option.value());
        } else
            config.logger.warn("Attempt to copy option value from non option config entry.");
    }
}
