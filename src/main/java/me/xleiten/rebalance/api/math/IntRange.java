package me.xleiten.rebalance.api.math;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import me.xleiten.rebalance.api.config.IType;
import org.jetbrains.annotations.NotNull;

public final class IntRange extends ValueRange<Integer>
{
    IntRange(Integer min, Integer max) {
        super(min, max);
    }

    @Override
    public boolean isIn(Integer value) {
        return value >= min && value <= max;
    }

    public static class Type implements IType<IntRange, JsonObject>
    {
        @Override
        public @NotNull String getTypeKey() {
            return "IR";
        }

        @Override
        public @NotNull Class<IntRange> getTypeClass() {
            return IntRange.class;
        }

        @Override
        public @NotNull IntRange deserialize(JsonObject element, JsonDeserializationContext context) {
            return new IntRange(element.get("min").getAsInt(), element.get("max").getAsInt());
        }

        @Override
        public @NotNull JsonObject serialize(IntRange range, JsonSerializationContext context) {
            var object = new JsonObject();
            object.add("min", new JsonPrimitive(range.min));
            object.add("max", new JsonPrimitive(range.max));
            return object;
        }
    }
}
