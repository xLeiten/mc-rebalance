package me.xleiten.rebalance.core.game.world.item;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import me.xleiten.rebalance.api.config.types.ObjectType;
import org.jetbrains.annotations.NotNull;

public record ArmorConfig(
        int bootsArmor,
        int legsArmor,
        int chestArmor,
        int helmArmor,
        int bodyArmor,
        float toughness,
        float knockbackResistance,
        int enchantability
) {
    public static class Type extends ObjectType<ArmorConfig>
    {
        @Override
        public @NotNull String getTypeKey() {
            return "AC";
        }

        @Override
        public @NotNull Class<ArmorConfig> getTypeClass() {
            return ArmorConfig.class;
        }

        @Override
        public @NotNull ArmorConfig deserialize(JsonObject element, JsonDeserializationContext context) {
            var armorObj = element.get("armor_points").getAsJsonObject();
            return new ArmorConfig(
                    armorObj.get("boots").getAsInt(),
                    armorObj.get("legs").getAsInt(),
                    armorObj.get("chest").getAsInt(),
                    armorObj.get("helm").getAsInt(),
                    armorObj.get("body").getAsInt(),
                    element.get("toughness").getAsFloat(),
                    element.get("knockback_resistance").getAsFloat(),
                    element.get("enchantability").getAsInt()
            );
        }

        @NotNull
        @Override
        public JsonObject serialize(ArmorConfig object, JsonSerializationContext context) {
            var obj = new JsonObject();
            var armorObj = new JsonObject();
            armorObj.addProperty("boots", object.bootsArmor);
            armorObj.addProperty("legs", object.chestArmor);
            armorObj.addProperty("chest", object.legsArmor);
            armorObj.addProperty("helm", object.helmArmor);
            armorObj.addProperty("body", object.bodyArmor);
            obj.add("armor_points", armorObj);
            obj.addProperty("toughness", object.toughness);
            obj.addProperty("knockback_resistance", object.knockbackResistance);
            obj.addProperty("enchantability", object.enchantability);
            return obj;
        }
    }
}
