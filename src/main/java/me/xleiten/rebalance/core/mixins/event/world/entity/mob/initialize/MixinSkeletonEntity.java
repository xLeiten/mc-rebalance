package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkeletonEntity.class)
public abstract class MixinSkeletonEntity extends AbstractSkeletonEntity {

    private MixinSkeletonEntity(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "convertToStray",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/SkeletonEntity;convertTo(Lnet/minecraft/entity/EntityType;Z)Lnet/minecraft/entity/mob/MobEntity;",
                    shift = At.Shift.AFTER
            )
    )
    public void onSkeletonConversion(CallbackInfo ci) {
        var world = (ServerWorld) getWorld();
        ((InitializableMobEntity) this).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.CONVERSION, getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(this, world, SpawnReason.CONVERSION, getPos());
    }

}
