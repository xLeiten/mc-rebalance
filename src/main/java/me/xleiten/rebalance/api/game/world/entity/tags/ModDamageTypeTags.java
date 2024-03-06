package me.xleiten.rebalance.api.game.world.entity.tags;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.RegistryKey;

public final class ModDamageTypeTags
{
    @SuppressWarnings("unchecked")
    public static final ImmutableSet<RegistryKey<DamageType>> ENTITY_ATTACK = new ImmutableSet.Builder<RegistryKey<DamageType>>().add(
            DamageTypes.MOB_ATTACK,
            DamageTypes.MOB_PROJECTILE,
            DamageTypes.MOB_ATTACK_NO_AGGRO,
            DamageTypes.PLAYER_ATTACK,
            DamageTypes.PLAYER_EXPLOSION,
            DamageTypes.SONIC_BOOM,
            DamageTypes.WITHER_SKULL,
            DamageTypes.INDIRECT_MAGIC,
            DamageTypes.FIREBALL,
            DamageTypes.STING,
            DamageTypes.THORNS
    ).build();
}
