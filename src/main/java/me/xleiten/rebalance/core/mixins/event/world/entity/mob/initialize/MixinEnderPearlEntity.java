package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderPearlEntity.class)
public abstract class MixinEnderPearlEntity {

    @Inject(
            method = "onCollision",
            at = @At(
                    value = "INVOKE",
                    target ="Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    public void onEnderPearlEndermiteSpawn(HitResult hitResult, CallbackInfo ci, @Local EndermiteEntity endermiteEntity) {
        var world = (ServerWorld) endermiteEntity.getWorld();
        ((InitializableMobEntity) endermiteEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.PROJECTILE, endermiteEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(endermiteEntity, world, SpawnReason.PROJECTILE, endermiteEntity.getPos());
    }

}
