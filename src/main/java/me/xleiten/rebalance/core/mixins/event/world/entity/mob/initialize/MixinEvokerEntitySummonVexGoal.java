package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/entity/mob/EvokerEntity$SummonVexGoal")
public abstract class MixinEvokerEntitySummonVexGoal {

    @Inject(
            method = "castSpell",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
            )
    )
    public void onVexSummon(CallbackInfo ci, @Local VexEntity vex) {
        var world = (ServerWorld) vex.getWorld();
        ((InitializableMobEntity) vex).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.MOB_SUMMONED, vex.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(vex, world, SpawnReason.MOB_SUMMONED, vex.getPos());
    }

}
