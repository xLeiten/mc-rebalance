package me.xleiten.rebalance.core.mixins.world.entity.damage_rebalance;

import net.minecraft.entity.DamageUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DamageUtil.class)
public abstract class MixinDamageUtil
{
    @ModifyArg(
            method = "getInflictedDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F"
            ),
            index = 2
    )
    private static float changeProtectionMaxLimit1(float value) {
        return 23f;
    }

    @ModifyArg(
            method = "getDamageLeft",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F"
            ),
            index = 2
    )
    private static float changeProtectionMaxLimit2(float value) {
        return 23f;
    }
}
