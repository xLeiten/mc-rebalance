package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
        value = SpiderEntity.class,
        priority = 999
)
public abstract class MixinSpiderEntity {

    @Inject(
            method = "initialize",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/SkeletonEntity;startRiding(Lnet/minecraft/entity/Entity;)Z",
                    shift = At.Shift.AFTER
            )
    )
    public void onSkeletonJockeySpawn(ServerWorldAccess world, LocalDifficulty difficulty, net.minecraft.entity.SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir, @Local SkeletonEntity skeletonEntity) {
        ((InitializableMobEntity) skeletonEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.JOCKEY, skeletonEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(skeletonEntity, world, SpawnReason.JOCKEY, skeletonEntity.getPos());
    }

}
