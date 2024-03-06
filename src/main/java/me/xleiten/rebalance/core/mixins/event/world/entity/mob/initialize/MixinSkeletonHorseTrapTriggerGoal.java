package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.ai.goal.SkeletonHorseTrapTriggerGoal;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkeletonHorseTrapTriggerGoal.class)
public abstract class MixinSkeletonHorseTrapTriggerGoal {

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V",
                    ordinal = 0
            )
    )
    public void onSkeletonHorseSpawn(CallbackInfo ci, @Local(ordinal = 0) SkeletonEntity skeletonEntity) {
        var world = (ServerWorld) skeletonEntity.getWorld();
        ((InitializableMobEntity) skeletonEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.TRIGGERED, skeletonEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(skeletonEntity, world, SpawnReason.TRIGGERED, skeletonEntity.getPos());
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V",
                    ordinal = 1
            )
    )
    public void onAhShitGroupSpawn(CallbackInfo ci, @Local AbstractHorseEntity abstractHorseEntity) {
        var world = (ServerWorld) abstractHorseEntity.getWorld();
        ((InitializableMobEntity) abstractHorseEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.TRIGGERED, abstractHorseEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(abstractHorseEntity, (ServerWorld) abstractHorseEntity.getWorld(), SpawnReason.TRIGGERED, abstractHorseEntity.getPos());
    }

}
