package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class MixinVillagerEntity {

    @Inject(
            method = "onStruckByLightning",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/passive/VillagerEntity;discard()V"
            )
    )
    public void onVillagerConversion(CallbackInfo ci, @Local WitchEntity witchEntity) {
        var world = (ServerWorld) witchEntity.getWorld();
        ((InitializableMobEntity) witchEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.CONVERSION, witchEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(witchEntity, world, SpawnReason.CONVERSION, witchEntity.getPos());
    }

}
