package me.xleiten.rebalance.core.mixins.event.world.entity;

import me.xleiten.rebalance.api.game.event.world.entity.EntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.SpawnableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerEntityManager.class)
public abstract class MixinServerEntityManager
{
    @Inject(
            method = "addEntity(Lnet/minecraft/world/entity/EntityLike;Z)Z",
            at = @At("TAIL")
    )
    public void onEntityAdded(EntityLike entityLike, boolean existing, CallbackInfoReturnable<Boolean> cir) {
        if (entityLike instanceof Entity entity) {
            var world = (ServerWorld) entity.getWorld();
            var pos = entity.getPos();
            ((SpawnableEntity) entity).cringeMod$onEntityAddedToWorld(world, pos);
            EntityEvents.SPAWN.invoker().onSpawn(entity, world, pos);
        }
    }
}
