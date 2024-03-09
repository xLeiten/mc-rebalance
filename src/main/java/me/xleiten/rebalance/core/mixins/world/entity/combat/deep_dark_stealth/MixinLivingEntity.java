package me.xleiten.rebalance.core.mixins.world.entity.combat.deep_dark_stealth;

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
        if (getWorld().getBiome(getBlockPos()).isIn(RebalanceBiomeTags.SCULK_BIOME)) {
            return value * 0.25;
        }
        return value;
    }
}
