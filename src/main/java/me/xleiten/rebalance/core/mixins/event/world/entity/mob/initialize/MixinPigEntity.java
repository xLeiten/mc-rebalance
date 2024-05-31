package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.world.entity.mob.Mob;
import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PigEntity.class)
public abstract class MixinPigEntity
{
    @Inject(
            method = "onStruckByLightning",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    public void onPigConversion(ServerWorld world, LightningEntity lightning, CallbackInfo ci, @Local ZombifiedPiglinEntity entity) {
        ((Mob) entity).rebalanceMod$onFirstSpawn(world, world.getRandom(), SpawnReason.CONVERSION);
    }
}
