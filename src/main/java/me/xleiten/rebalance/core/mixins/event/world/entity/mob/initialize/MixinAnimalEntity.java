package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalEntity.class)
public abstract class MixinAnimalEntity {

    @Inject(
            method = "breed(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/AnimalEntity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
            )
    )
    public void onAnimalBreed(ServerWorld world, AnimalEntity other, CallbackInfo ci, @Local PassiveEntity passiveEntity) {
        ((InitializableMobEntity) passiveEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.BREEDING, passiveEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(passiveEntity, world, SpawnReason.BREEDING, passiveEntity.getPos());
    }

}
