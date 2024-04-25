package me.xleiten.rebalance.core.mixins.world.entity.mob.vision_rework;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.world.biome.tag.RebalanceBiomeTags;
import me.xleiten.rebalance.api.game.world.entity.tag.RebalanceEntityTypeTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity
{
    protected MixinLivingEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @ModifyVariable(
            method = "getAttackDistanceScalingFactor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;isSneaky()Z"
            ),
            index = 2
    )
    protected double changeDistanceScalingFactor(double value, @Local(argsOnly = true) Entity entity) {
        if (entity instanceof LivingEntity living) {
            var world = getWorld();
            var pos = getBlockPos();
            var entityType = living.getType();
            var lightLevel = world.getLightLevel(pos);
            var hasNightVision = living.getStatusEffect(StatusEffects.NIGHT_VISION) != null || entityType.isIn(RebalanceEntityTypeTags.CAN_FULLY_SEE_IN_DARKNESS);

            if (!hasNightVision) {
                value *= 1 - (6 - lightLevel) * 0.02;
            }

            if (!entityType.isIn(RebalanceEntityTypeTags.VISION_NOT_AFFECTED_BY_SCULK) && world.getBiome(pos).isIn(RebalanceBiomeTags.SCULK_BIOME)) {
                value *= 0.5 + (hasNightVision ? 0.5 : (lightLevel / 15d) * 0.2);
            }
        }
        return value;
    }
}
