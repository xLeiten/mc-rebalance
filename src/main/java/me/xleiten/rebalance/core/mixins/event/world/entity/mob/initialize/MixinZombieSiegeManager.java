package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.ZombieSiegeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieSiegeManager.class)
public abstract class MixinZombieSiegeManager {

    @Inject(
            method = "trySpawnZombie",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
            )
    )
    public void onZombieSiegeSpawn(ServerWorld world, CallbackInfo ci, @Local ZombieEntity zombie) {
        ((InitializableMobEntity) zombie).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.EVENT, zombie.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(zombie, world, SpawnReason.EVENT, zombie.getPos());
    }

}
