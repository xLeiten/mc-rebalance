package me.xleiten.rebalance.api.game.world.tag;

import me.xleiten.rebalance.Rebalance;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class RebalanceEntityTypeTags
{
    public static final TagKey<EntityType<?>> BOSSES = create("bosses");
    public static final TagKey<EntityType<?>> GUARDIANS = create("guardians");
    public static final TagKey<EntityType<?>> GOLEMS = create("golems");

    public static final TagKey<EntityType<?>> NOT_AFFECTED_BY_HEALTH_SLOWDOWN = create("not_affected_by_health_slowdown");
    public static final TagKey<EntityType<?>> CANNOT_REGEN_HEALTH = create("cannot_regen_health");
    public static final TagKey<EntityType<?>> WITHOUT_HEALTH_DISPLAY = create("without_health_display");
    public static final TagKey<EntityType<?>> CAN_FULLY_SEE_IN_DARKNESS = create("can_fully_see_in_darkness");
    public static final TagKey<EntityType<?>> VISION_NOT_AFFECTED_BY_SCULK = create("vision_not_affected_by_sculk");

    private static TagKey<EntityType<?>> create(String name) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Rebalance.MOD_ID, name));
    }
}
