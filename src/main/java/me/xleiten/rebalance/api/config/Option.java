package me.xleiten.rebalance.api.config;

import org.jetbrains.annotations.NotNull;

public final class Option<T> extends DynamicStorageEntry
{
    public final String name;
    private T value;

    Option(String name, T value, DynamicStorage config) {
        super(config);
        this.name = name;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(@NotNull T value) {
        this.value = value;
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
