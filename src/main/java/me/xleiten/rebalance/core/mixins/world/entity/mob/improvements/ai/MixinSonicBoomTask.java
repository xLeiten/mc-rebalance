package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements.ai;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.math.RandomHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.SonicBoomTask;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WardenEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SonicBoomTask.class)
public abstract class MixinSonicBoomTask
{
    @WrapOperation(
            method = "method_43265",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    private static boolean onDamageEntity(LivingEntity instance, DamageSource source, float amount, Operation<Boolean> original, @Local(argsOnly = true) WardenEntity warden) {
        return original.call(instance, source, amount * RandomHelper.range(warden.getRandom(), Settings.MOB_WARDEN__SONIC_BOOM_DAMAGE_MULT.value()));
    }
}
