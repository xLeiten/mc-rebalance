package me.xleiten.rebalance.util.math;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import me.xleiten.rebalance.api.config.IType;
import org.jetbrains.annotations.NotNull;

public final class DoubleRange extends ValueRange<Double>
{
    DoubleRange(Double min, Double max) {
        super(min, max);
    }

    @Override
    public boolean isIn(Double value) {
        return value >= min && value <= max;
    }

    public static class Type implements IType<DoubleRange, JsonObject>
    {
        @Override
        public @NotNull String getTypeKey() {
            return "DR";
        }

        @Override
        public @NotNull Class<DoubleRange> getTypeClass() {
            return DoubleRange.class;
        }

        @Override
        public @NotNull DoubleRange deserialize(JsonObject element, JsonDeserializationContext context) {
            return new DoubleRange(element.get("min").getAsDouble(), element.get("max").getAsDouble());
        }

        @Override
        public @NotNull JsonObject serialize(DoubleRange object, JsonSerializationContext context) {
            var obj = new JsonObject();
            obj.add("min", new JsonPrimitive(object.min));
            obj.add("max", new JsonPrimitive(object.max));
            return obj;
        }
    }
}
