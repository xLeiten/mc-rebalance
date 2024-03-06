package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.block.FrogspawnBlock;
import net.minecraft.entity.passive.TadpoleEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FrogspawnBlock.class)
public abstract class MixinFrogspawnBlock {

    @Inject(
            method = "spawnTadpoles",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    private void onTadpolesSpawn(ServerWorld world, BlockPos pos, Random random, CallbackInfo ci, @Local TadpoleEntity tadpoleEntity) {
        ((InitializableMobEntity) tadpoleEntity).cringeMod$onMobInitialize(world, random, SpawnReason.BREEDING, tadpoleEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(tadpoleEntity, world, SpawnReason.BREEDING, tadpoleEntity.getPos());
    }

}
