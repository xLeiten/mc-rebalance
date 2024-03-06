package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.block.InfestedBlock;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InfestedBlock.class)
public abstract class MixinInfestedBlock {

    @Inject(
            method = "spawnSilverfish",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    private void onSilverfishSpawn(ServerWorld world, BlockPos pos, CallbackInfo ci, @Local SilverfishEntity silverfishEntity) {
        ((InitializableMobEntity) silverfishEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.BLOCK_CHANGE, silverfishEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(silverfishEntity, world, SpawnReason.BLOCK_CHANGE, silverfishEntity.getPos());
    }

}
