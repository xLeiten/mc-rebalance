package me.xleiten.rebalance.api.config.types;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import org.jetbrains.annotations.NotNull;

public final class BoolType extends PrimitiveType<Boolean> {
    @Override
    public @NotNull String getTypeKey() {
        return "B";
    }

    @Override
    public @NotNull Class<Boolean> getTypeClass() {
        return Boolean.class;
    }

    @Override
    public @NotNull Boolean deserialize(JsonPrimitive element, JsonDeserializationContext context) {
        return element.getAsBoolean();
    }

    @Override
    public @NotNull JsonPrimitive serialize(Boolean object, JsonSerializationContext context) {
        return new JsonPrimitive(object);
    }
}
