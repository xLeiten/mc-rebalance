package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements.ai;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MeleeAttackGoal.class)
public abstract class MixinMeleeAttackGoal
{
    @Shadow @Final protected PathAwareEntity mob;
    @Shadow @Final private double speed;

    @Unique private int fastMoveTicks = 3;

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ai/goal/MeleeAttackGoal;attack(Lnet/minecraft/entity/LivingEntity;)V"
            )
    )
    public void onTick(CallbackInfo ci) {
        if (fastMoveTicks-- <= 0) {
            mob.getNavigation().startMovingTo(mob.getTarget(), speed);
            fastMoveTicks = mob.getRandom().nextBetween(1, 3);
        }
    }
}
