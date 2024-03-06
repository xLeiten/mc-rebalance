package me.xleiten.rebalance.core.mixins.event.world.entity.player;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.xleiten.rebalance.api.game.event.world.entity.player.ServerPlayerEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity
{
    @WrapOperation(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;onDeath(Lnet/minecraft/entity/damage/DamageSource;)V"
            )
    )
    public void onPlayerDeath(LivingEntity instance, DamageSource damageSource, Operation<Void> original) {
        if (instance instanceof ServerPlayerEntity player) {
            var world = player.getServerWorld();
            var result = ServerPlayerEvents.DEATH.invoker().onDeath(player, world, world.getServer(), damageSource);
            if (result == ActionResult.FAIL)
                return;
        }
        original.call(instance, damageSource);
    }
}
