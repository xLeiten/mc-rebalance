package me.xleiten.rebalance.api.config.types;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;

public final class DoubleType extends NumberType<Double> {
    @Override
    public @NotNull String getTypeKey() {
        return "D";
    }

    @Override
    public @NotNull Class<Double> getTypeClass() {
        return Double.class;
    }

    @Override
    public @NotNull Double deserialize(JsonPrimitive element, JsonDeserializationContext context) {
        return element.getAsDouble();
    }
}
