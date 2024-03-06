package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EggEntity.class)
public abstract class MixinEggEntity {

    @Inject(
            method = "onCollision",
            at = @At(
                    value = "INVOKE",
                    target ="Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    public void onEggChickenSpawn(HitResult hitResult, CallbackInfo ci, @Local ChickenEntity chickenEntity) {
        var world = (ServerWorld) chickenEntity.getWorld();
        ((InitializableMobEntity) chickenEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.PROJECTILE, chickenEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(chickenEntity, world, SpawnReason.PROJECTILE, chickenEntity.getPos());
    }

}
