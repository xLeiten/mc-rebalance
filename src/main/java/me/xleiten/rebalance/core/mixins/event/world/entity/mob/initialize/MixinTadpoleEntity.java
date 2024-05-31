package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.world.entity.mob.Mob;
import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.TadpoleEntity;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TadpoleEntity.class)
public abstract class MixinTadpoleEntity
{
    @Inject(
            method = "growUp",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/passive/TadpoleEntity;discard()V"
            )
    )
    public void onTadpoleGrowUp(CallbackInfo ci, @Local FrogEntity entity) {
        var world = (ServerWorldAccess) entity.getWorld();
        ((Mob) entity).rebalanceMod$onFirstSpawn(world, world.getRandom(), SpawnReason.REINFORCEMENT);
    }
}
