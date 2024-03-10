package me.xleiten.rebalance.core.mixins.world.entity.combat.target_entity_visibility;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.world.biome.tag.RebalanceBiomeTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
    protected double changeDistanceScalingFactorIfInDeepDark(double value, @Local(argsOnly = true) Entity entity) {
        var world = getWorld();
        var pos = getBlockPos();
        var lightLevel = world.getLightLevel(pos);
        value *= 1 - (15 - lightLevel) * 0.015;
        if (world.getBiome(pos).isIn(RebalanceBiomeTags.SCULK_BIOME)) {
            value *= 0.5 + (lightLevel / 15d) * 0.5;
        }
        return value;
    }
}
