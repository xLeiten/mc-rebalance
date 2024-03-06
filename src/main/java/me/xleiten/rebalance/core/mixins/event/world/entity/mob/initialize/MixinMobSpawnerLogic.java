package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.entity.Entity;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobSpawnerLogic.class)
public abstract class MixinMobSpawnerLogic {

    @Inject(
            method = "serverTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/EntityData;",
                    shift = At.Shift.AFTER
            )
    )
    public void onSpawnerSpawn(ServerWorld world, BlockPos pos, CallbackInfo ci, @Local Entity mobEntity) {
        ((InitializableMobEntity) mobEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.SPAWNER, mobEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize((MobEntity) mobEntity, world, SpawnReason.SPAWNER, mobEntity.getPos());
    }

}
