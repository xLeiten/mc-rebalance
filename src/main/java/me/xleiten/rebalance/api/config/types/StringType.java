package me.xleiten.rebalance.api.config.types;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import org.jetbrains.annotations.NotNull;

public final class StringType extends PrimitiveType<String>
{
    @Override
    public @NotNull String getTypeKey() {
        return "S";
    }

    @Override
    public @NotNull Class<String> getTypeClass() {
        return String.class;
    }

    @Override
    public @NotNull String deserialize(JsonPrimitive element, JsonDeserializationContext context) {
        return element.getAsString();
    }

    @Override
    public @NotNull JsonPrimitive serialize(String object, JsonSerializationContext context) {
        return new JsonPrimitive(object);
    }
}
