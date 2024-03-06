package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerEntity.class)
public abstract class MixinShulkerEntity {

    @Inject(
            method = "spawnNewShulker",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    public void onShulkerDuplicate(CallbackInfo ci, @Local ShulkerEntity shulkerEntity) {
        var world = (ServerWorld) shulkerEntity.getWorld();
        ((InitializableMobEntity) shulkerEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.DUPLICATE, shulkerEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(shulkerEntity, world, SpawnReason.DUPLICATE, shulkerEntity.getPos());
    }

}
