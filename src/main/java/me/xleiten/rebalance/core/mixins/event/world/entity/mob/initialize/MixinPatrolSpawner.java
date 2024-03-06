package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.spawner.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PatrolSpawner.class)
public abstract class MixinPatrolSpawner {

    @Inject(
            method = "spawnPillager",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
            )
    )
    public void onPatrolSpawn(ServerWorld world, BlockPos pos, Random random, boolean captain, CallbackInfoReturnable<Boolean> cir, @Local PatrolEntity patrolEntity) {
        ((InitializableMobEntity) patrolEntity).cringeMod$onMobInitialize(world, random, SpawnReason.PATROL, patrolEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(patrolEntity, world, SpawnReason.PATROL, patrolEntity.getPos());
    }

}
