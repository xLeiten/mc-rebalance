package me.xleiten.rebalance.api.game.world.entity.tags;

import me.xleiten.rebalance.Rebalance;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class ModEntityTypeTags
{
    public static final TagKey<EntityType<?>> BOSSES = create("bosses");
    public static final TagKey<EntityType<?>> GUARDIANS = create("guardians");
    public static final TagKey<EntityType<?>> GOLEMS = create("golems");

    public static final TagKey<EntityType<?>> NOT_AFFECTED_BY_HEALTH_SLOWDOWN = create("not_affected_by_health_slowdown");
    public static final TagKey<EntityType<?>> CANNOT_REGEN_HEALTH = create("cannot_regen_health");
    public static final TagKey<EntityType<?>> WITHOUT_HEALTH_DISPLAY = create("without_health_display");

    public static final TagKey<EntityType<?>> CAN_MINE_BLOCKS = create("can_mine_blocks");
    public static final TagKey<EntityType<?>> CAN_PLACE_BLOCKS = create("can_place_blocks");

    private static TagKey<EntityType<?>> create(String name) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Rebalance.MOD_ID, name));
    }
}
