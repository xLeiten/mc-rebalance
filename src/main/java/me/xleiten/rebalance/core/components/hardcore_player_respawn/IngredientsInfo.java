package me.xleiten.rebalance.core.components.hardcore_player_respawn;

import com.google.gson.*;
import me.xleiten.rebalance.api.config.types.ArrayType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class IngredientsInfo
{
    private final Map<Identifier, Integer> info;

    public IngredientsInfo() {
        this.info = new HashMap<>();
    }

    IngredientsInfo(Map<Identifier, Integer> ingredients)
    {
        this.info = ingredients;
    }

    public IngredientsInfo addIngredient(Item item, int amount) {
        this.info.putIfAbsent(Registries.ITEM.getId(item), amount);
        return this;
    }

    public Set<Map.Entry<Identifier, Integer>> getInfo() {
        return info.entrySet();
    }

    public static class Type extends ArrayType<IngredientsInfo>
    {
        @Override
        public @NotNull String getTypeKey() {
            return "IL";
        }

        @Override
        public @NotNull Class<IngredientsInfo> getTypeClass() {
            return IngredientsInfo.class;
        }

        @Override
        public @NotNull IngredientsInfo deserialize(JsonArray array, JsonDeserializationContext context) {
            Map<Identifier, Integer> data = new HashMap<>();
            for (JsonElement element: array) {
                if (element instanceof JsonObject object)
                    data.put(Identifier.tryParse(object.get("item").getAsString()), object.get("amount").getAsInt());
            }
            return new IngredientsInfo(data);
        }

        @NotNull
        @Override
        public JsonArray serialize(IngredientsInfo ingredients, JsonSerializationContext context) {
            var array = new JsonArray();
            for (Map.Entry<Identifier, Integer> entry: ingredients.info.entrySet()) {
                var object = new JsonObject();
                object.addProperty("item", entry.getKey().toString());
                object.addProperty("amount", entry.getValue());
                array.add(object);
            }
            return array;
        }
    }
}
