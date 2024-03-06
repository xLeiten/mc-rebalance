package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs.ai;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.util.math.DoubleRange;
import me.xleiten.rebalance.util.math.RandomHelper;
import me.xleiten.rebalance.util.math.Range;
import net.minecraft.entity.ai.brain.task.SonicBoomTask;
import net.minecraft.entity.mob.WardenEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static me.xleiten.rebalance.Settings.WARDEN_SETTINGS;

@Mixin(SonicBoomTask.class)
public abstract class MixinSonicBoomTask
{
    @Unique private static Option<DoubleRange> DAMAGE_MULT = WARDEN_SETTINGS.option("sonic-boom-damage-mult", Range.create(1.5, 2));

    @ModifyArg(
            method = "method_43265",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            ),
            index = 1
    )
    private static float onDamageEntity(float amount, @Local WardenEntity wardenEntity) {
        return amount * (float) RandomHelper.range(wardenEntity.getRandom(), DAMAGE_MULT.getValue());
    }
}
