package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CarvedPumpkinBlock.class)
public abstract class MixinCarvedPumpkinBlock {

    @Inject(
            method = "trySpawnEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/CarvedPumpkinBlock;spawnEntity(Lnet/minecraft/world/World;Lnet/minecraft/block/pattern/BlockPattern$Result;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void onSnowGolemBuild(World world, BlockPos pos, CallbackInfo ci, @Local SnowGolemEntity snowGolemEntity) {
        ((InitializableMobEntity) snowGolemEntity).cringeMod$onMobInitialize((ServerWorld) world, world.getRandom(), SpawnReason.GOLEM, snowGolemEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(snowGolemEntity, (ServerWorld) world, SpawnReason.GOLEM, snowGolemEntity.getPos());
    }

    @Inject(
            method = "trySpawnEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/CarvedPumpkinBlock;spawnEntity(Lnet/minecraft/world/World;Lnet/minecraft/block/pattern/BlockPattern$Result;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void onIronGolemBuild(World world, BlockPos pos, CallbackInfo ci, @Local IronGolemEntity ironGolemEntity) {
        ((InitializableMobEntity) ironGolemEntity).cringeMod$onMobInitialize((ServerWorld) world, world.getRandom(), SpawnReason.GOLEM, ironGolemEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(ironGolemEntity, (ServerWorld) world, SpawnReason.GOLEM, ironGolemEntity.getPos());
    }

}
