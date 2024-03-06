package me.xleiten.rebalance.util.math;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import me.xleiten.rebalance.api.config.IType;
import org.jetbrains.annotations.NotNull;

public final class FloatRange extends ValueRange<Float>
{
    public FloatRange(Float min, Float max) {
        super(min, max);
    }

    @Override
    public boolean isIn(Float value) {
        return value >= min && value <= max;
    }

    public static class Type implements IType<FloatRange, JsonObject>
    {
        @Override
        public @NotNull String getTypeKey() {
            return "IR";
        }

        @Override
        public @NotNull Class<FloatRange> getTypeClass() {
            return FloatRange.class;
        }

        @Override
        public @NotNull FloatRange deserialize(JsonObject element, JsonDeserializationContext context) {
            return new FloatRange(element.get("min").getAsFloat(), element.get("max").getAsFloat());
        }

        @Override
        public @NotNull JsonObject serialize(FloatRange range, JsonSerializationContext context) {
            var object = new JsonObject();
            object.add("min", new JsonPrimitive(range.min));
            object.add("max", new JsonPrimitive(range.max));
            return object;
        }
    }
}
