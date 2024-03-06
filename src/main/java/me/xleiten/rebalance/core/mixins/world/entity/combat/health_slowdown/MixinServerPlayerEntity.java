package me.xleiten.rebalance.core.mixins.world.entity.combat.health_slowdown;

import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.config.Option;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends MixinLivingEntity
{
    @Unique private static final Option<Float> HEALTH_RATIO_MULTIPLIER = Settings.PLAYER_SETTINGS.option("health-slowdown-ratio-mult", 0.6f);
    @Unique private static final Option<Float> HEALTH_SLOWDOWN_STOP_LIMIT = Settings.PLAYER_SETTINGS.option("health-slowdown-stop-limit", 0.65f);

    protected MixinServerPlayerEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Override
    public float getHealthRatioMult() {
        return HEALTH_RATIO_MULTIPLIER.getValue();
    }

    @Override
    public float getHealthSlowdownStopLimit() {
        return HEALTH_SLOWDOWN_STOP_LIMIT.getValue();
    }
}
