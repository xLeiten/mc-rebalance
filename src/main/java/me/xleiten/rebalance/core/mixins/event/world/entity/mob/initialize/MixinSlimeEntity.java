package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeEntity.class)
public abstract class MixinSlimeEntity {

    @Inject(
            method = "remove",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    public void onSlimeDuplicate(Entity.RemovalReason reason, CallbackInfo ci, @Local SlimeEntity slimeEntity) {
        var world = (ServerWorld) slimeEntity.getWorld();
        ((InitializableMobEntity) slimeEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.DUPLICATE, slimeEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(slimeEntity, world, SpawnReason.DUPLICATE, slimeEntity.getPos());
    }

}
