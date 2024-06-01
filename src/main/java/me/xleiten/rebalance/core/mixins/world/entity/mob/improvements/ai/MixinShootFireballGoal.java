package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements.ai;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.entity.ai.goal.Goal;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/entity/mob/GhastEntity$ShootFireballGoal")
public abstract class MixinShootFireballGoal extends Goal
{
    @Shadow public int cooldown;

    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/mob/GhastEntity$ShootFireballGoal;cooldown:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 2
            )
    )
    private int allowMultipleFireballs(int original, @Share("thirdFireball") LocalBooleanRef ref) {
        return original == 20 || original == 40 || original == 60 ? 20 : original;
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z",
                    shift = At.Shift.AFTER
            )
    )
    private void changeResultCooldown1(CallbackInfo ci, @Share("temp") LocalIntRef temp) {
        temp.set(cooldown);
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/mob/GhastEntity$ShootFireballGoal;cooldown:I",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER,
                    ordinal = 1
            )
    )
    private void changeResultCooldown2(CallbackInfo ci, @Share("temp") LocalIntRef temp, @Share("thirdFireball") LocalBooleanRef thirdFireball) {
        int result = temp.get();
        this.cooldown = result < 60 ? result : -40;
    }
}
