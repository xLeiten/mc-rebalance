package me.xleiten.rebalance.api.config.types;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;

public final class LongType extends NumberType<Long>
{
    @Override
    public @NotNull String getTypeKey() {
        return "L";
    }

    @Override
    public @NotNull Class<Long> getTypeClass() {
        return Long.class;
    }

    @Override
    public @NotNull Long deserialize(JsonPrimitive element, JsonDeserializationContext context) {
        return element.getAsLong();
    }
}
