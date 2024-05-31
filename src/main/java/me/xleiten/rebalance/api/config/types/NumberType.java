package me.xleiten.rebalance.api.config.types;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import org.jetbrains.annotations.NotNull;

public abstract class NumberType<T extends Number> extends PrimitiveType<T>
{
    @Override
    public @NotNull JsonPrimitive serialize(T object, JsonSerializationContext context) {
        return new JsonPrimitive(object);
    }
}
