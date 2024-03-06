package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.TadpoleEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TadpoleEntity.class)
public abstract class MixinTadpoleEntity {

    @Inject(
            method = "growUp",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/passive/TadpoleEntity;discard()V"
            )
    )
    public void onTadpoleGrowUp(CallbackInfo ci, @Local FrogEntity frogEntity) {
        var world = (ServerWorld) frogEntity.getWorld();
        ((InitializableMobEntity) frogEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.CONVERSION, frogEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(frogEntity, world, SpawnReason.CONVERSION, frogEntity.getPos());
    }

}
