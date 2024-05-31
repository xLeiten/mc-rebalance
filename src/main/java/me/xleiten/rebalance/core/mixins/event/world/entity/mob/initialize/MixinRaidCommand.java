package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.world.entity.mob.Mob;
import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.command.RaidCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RaidCommand.class)
public abstract class MixinRaidCommand
{
    @Inject(
            method = "executeSpawnLeader",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V",
                    shift = At.Shift.AFTER
            )
    )
    private static void onRaidLeaderSpawn(ServerCommandSource source, CallbackInfoReturnable<Integer> cir, @Local RaiderEntity entity) {
        var world = (ServerWorldAccess) entity.getWorld();
        ((Mob) entity).rebalanceMod$onFirstSpawn(world, world.getRandom(), SpawnReason.COMMAND);
    }
}
