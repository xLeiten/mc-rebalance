package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MixinMobEntity implements InitializableMobEntity {

    @Inject(
            method = "method_24522",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;onPlayerSpawnedChild(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/mob/MobEntity;)V",
                    shift = At.Shift.AFTER
            )
    )
    public void onSpawnEggBabySpawn(PlayerEntity playerEntity, MobEntity entity, CallbackInfo ci) {
        var world = (ServerWorld) entity.getWorld();
        ((InitializableMobEntity) entity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.SPAWN_EGG, entity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(entity, world, SpawnReason.SPAWN_EGG, entity.getPos());
    }

}
