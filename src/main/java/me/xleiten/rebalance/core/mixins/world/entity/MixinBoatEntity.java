package me.xleiten.rebalance.core.mixins.world.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Debug(export = true)
@Mixin(BoatEntity.class)
public abstract class MixinBoatEntity extends Entity
{
    @Shadow private double fallVelocity;

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

    @Override
    public void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
        this.fallVelocity = this.getVelocity().y;
        if (!this.hasVehicle()) {
            if (onGround) {
                /*if (this.fallDistance > 3.0F) {
                    if (this.location != BoatEntity.Location.ON_LAND) {
                        this.onLanding();
                        return;
                    }

                    this.handleFallDamage(this.fallDistance, 1.0F, this.getDamageSources().fall());
                    if (!this.getWorld().isClient && !this.isRemoved()) {
                        this.kill();
                        if (this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                            for(int i = 0; i < 3; ++i) {
                                this.dropItem(this.getVariant().getBaseBlock());
                            }

                            for(int i = 0; i < 2; ++i) {
                                this.dropItem(Items.STICK);
                            }
                        }
                    }
                }*/
                this.onLanding();
            } else if (!this.getWorld().getFluidState(this.getBlockPos().down()).isIn(FluidTags.WATER) && heightDifference < 0.0) {
                this.fallDistance -= (float)heightDifference;
            }
        }
    }
}
