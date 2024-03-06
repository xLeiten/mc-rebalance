package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TurtleEggBlock.class)
public abstract class MixinTurtleEggBlock {

    @Inject(
            method = "randomTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    public void onTurtleBreed(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci, @Local TurtleEntity turtleEntity) {
        ((InitializableMobEntity) turtleEntity).cringeMod$onMobInitialize(world, random, SpawnReason.BREEDING, turtleEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(turtleEntity, world, SpawnReason.BREEDING, turtleEntity.getPos());
    }

}
