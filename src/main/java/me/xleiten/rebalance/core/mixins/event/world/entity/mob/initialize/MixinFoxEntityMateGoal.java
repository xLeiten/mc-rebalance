package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/entity/passive/FoxEntity$MateGoal")
public abstract class MixinFoxEntityMateGoal {

    @Inject(
            method = "breed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
            )
    )
    public void onFoxBreed(CallbackInfo ci, @Local FoxEntity fox) {
        var world = (ServerWorld) fox.getWorld();
        ((InitializableMobEntity) fox).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.BREEDING, fox.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(fox, world, SpawnReason.BREEDING, fox.getPos());
    }

}
