package me.xleiten.rebalance.api.config.types;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;

public final class FloatType extends NumberType<Float>
{
    @Override
    public @NotNull String getTypeKey() {
        return "F";
    }

    @Override
    public @NotNull Class<Float> getTypeClass() {
        return Float.class;
    }

    @Override
    public @NotNull Float deserialize(JsonPrimitive element, JsonDeserializationContext context) {
        return element.getAsFloat();
    }
}
