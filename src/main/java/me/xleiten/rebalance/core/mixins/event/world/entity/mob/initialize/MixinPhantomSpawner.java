package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomSpawner.class)
public abstract class MixinPhantomSpawner {

    @Inject(
            method = "spawn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
            )
    )
    public void onPhantomSpawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals, CallbackInfoReturnable<Integer> cir, @Local PhantomEntity phantomEntity) {
        ((InitializableMobEntity) phantomEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.NATURAL, phantomEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(phantomEntity, world, SpawnReason.NATURAL, phantomEntity.getPos());
    }

}
