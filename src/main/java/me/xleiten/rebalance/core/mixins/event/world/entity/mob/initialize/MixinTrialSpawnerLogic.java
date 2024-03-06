package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

@Mixin(TrialSpawnerLogic.class)
public abstract class MixinTrialSpawnerLogic {

    @Inject(
            method = "trySpawnMob",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;setPersistent()V",
                    shift = At.Shift.AFTER
            )
    )
    public void onTrialSpawnerSpawn(ServerWorld world, BlockPos pos, CallbackInfoReturnable<Optional<UUID>> cir, @Local MobEntity mobEntity) {
        ((InitializableMobEntity) mobEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.TRIAL_SPAWNER, mobEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(mobEntity, world, SpawnReason.TRIAL_SPAWNER, mobEntity.getPos());
    }

}
