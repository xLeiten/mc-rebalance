package me.xleiten.rebalance.core.mixins.world.entity.health_slowdown;

import me.xleiten.rebalance.Settings;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends MixinLivingEntity
{
    protected MixinServerPlayerEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Override
    public float getEffectMultiplier() {
        return Settings.SLOWDOWN_BY_HEALTH__PLAYER__EFFECT_MULTIPLIER.value();
    }

    @Override
    public float getAffectionHealthLimit() {
        return Settings.SLOWDOWN_BY_HEALTH__PLAYER__AFFECTION_HEALTH_LIMIT.value();
    }
}
