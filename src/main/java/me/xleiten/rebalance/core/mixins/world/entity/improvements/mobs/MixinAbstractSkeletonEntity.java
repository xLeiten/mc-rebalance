package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs;

import net.minecraft.entity.mob.AbstractSkeletonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AbstractSkeletonEntity.class)
public abstract class MixinAbstractSkeletonEntity
{
    @ModifyArgs(
            method = "shootAt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setVelocity(DDDFF)V"
            )
    )
    public void setArrowVelocity(Args args) {

    }
}
