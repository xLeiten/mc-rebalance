package me.xleiten.rebalance.api.config.types;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;

public final class IntType extends NumberType<Integer>
{
    @Override
    public @NotNull String getTypeKey() {
        return "I";
    }

    @Override
    public @NotNull Class<Integer> getTypeClass() {
        return Integer.class;
    }

    @Override
    public @NotNull Integer deserialize(JsonPrimitive element, JsonDeserializationContext context) {
        return element.getAsInt();
    }
}
