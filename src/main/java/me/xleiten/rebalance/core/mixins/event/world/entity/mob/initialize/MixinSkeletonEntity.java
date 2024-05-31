package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.xleiten.rebalance.api.game.world.entity.mob.Mob;
import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SkeletonEntity.class)
public abstract class MixinSkeletonEntity extends AbstractSkeletonEntity implements Mob
{
    private MixinSkeletonEntity(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @WrapOperation(
            method = "convertToStray",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/SkeletonEntity;convertTo(Lnet/minecraft/entity/EntityType;Z)Lnet/minecraft/entity/mob/MobEntity;"
            )
    )
    public MobEntity onSkeletonConversion(SkeletonEntity instance, EntityType<? extends SkeletonEntity> entityType, boolean b, Operation<MobEntity> original) {
        var entity = original.call(instance, entityType, b);
        var world = (ServerWorldAccess) entity.getWorld();
        ((Mob) entity).rebalanceMod$onFirstSpawn(world, world.getRandom(), SpawnReason.CONVERSION);
        return entity;
    }
}
