package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.spawner.CatSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatSpawner.class)
public abstract class MixinCatSpawner {

    @Inject(
            method = "spawn(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/server/world/ServerWorld;)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
            )
    )
    public void onRandomCatSpawn(BlockPos pos, ServerWorld world, CallbackInfoReturnable<Integer> cir, @Local CatEntity catEntity) {
        ((InitializableMobEntity) catEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.NATURAL, catEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(catEntity, world, SpawnReason.NATURAL, catEntity.getPos());
    }

}
