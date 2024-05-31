package me.xleiten.rebalance.core.mixins.world.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public abstract class MixinBoatEntity extends Entity
{
    protected MixinBoatEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Redirect(
            method = "tick",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/entity/vehicle/BoatEntity;getControllingPassenger()Lnet/minecraft/entity/LivingEntity;"
            )
    )
    protected LivingEntity removePlayerInBoatCheckForOtherEntities(BoatEntity instance) {
        return null;
    }

    @Inject(
            method = "fall",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/vehicle/BoatEntity;fallDistance:F",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void removeFallDamage(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition, CallbackInfo ci) {
        this.onLanding();
        ci.cancel();
    }
}
