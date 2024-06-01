package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements.ai;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.Settings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/entity/mob/ShulkerEntity$ShootBulletGoal")
public abstract class MixinShootBulletGoal
{
    @Shadow private int counter;
    @Shadow @Final ShulkerEntity field_7348;

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ShulkerEntity;getWorld()Lnet/minecraft/world/World;",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void shootAnotherBullet(CallbackInfo ci, @Local LivingEntity target) {
        if (Settings.MOB_SHULKER__SHOOT_SECOND_BULLET.value())
            field_7348.getWorld().spawnEntity(new ShulkerBulletEntity(field_7348.getWorld(), field_7348, target, field_7348.getAttachedFace().getOpposite().getAxis()));
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/mob/ShulkerEntity$ShootBulletGoal;counter:I",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER,
                    ordinal = 1
            )
    )
    private void changeNextShootDelay(CallbackInfo ci) {
        this.counter -= Math.min(20, Settings.MOB_SHULKER__BULLET_COOLDOWN_DECREASE.value());
    }
}
