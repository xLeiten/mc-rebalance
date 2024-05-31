package me.xleiten.rebalance.api.config.types;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;

public final class ShortType extends NumberType<Short>
{
    @Override
    public @NotNull String getTypeKey() {
        return "s";
    }

    @Override
    public @NotNull Class<Short> getTypeClass() {
        return Short.class;
    }

    @Override
    public @NotNull Short deserialize(JsonPrimitive element, JsonDeserializationContext context) {
        return element.getAsShort();
    }
}
