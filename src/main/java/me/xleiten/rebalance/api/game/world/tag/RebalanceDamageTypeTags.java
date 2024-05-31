package me.xleiten.rebalance.api.game.world.tag;

import me.xleiten.rebalance.Rebalance;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class RebalanceDamageTypeTags
{
    public static final TagKey<DamageType> SUMMONS_ZOMBIE_REINFORCEMENT = create("summons_zombie_reinforcement");

    private static TagKey<DamageType> create(String name) {
        return TagKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Rebalance.MOD_ID, name));
    }
}
