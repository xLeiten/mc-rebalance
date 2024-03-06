package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.command.RaidCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RaidCommand.class)
public abstract class MixinRaidCommand {

    @Inject(
            method = "executeSpawnLeader",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/raid/RaiderEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/EntityData;",
                    shift = At.Shift.AFTER
            )
    )
    private static void onRaidLeaderSpawn(ServerCommandSource source, CallbackInfoReturnable<Integer> cir, @Local RaiderEntity raider) {
        var world = source.getWorld();
        ((InitializableMobEntity) raider).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.COMMAND, raider.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(raider, world, SpawnReason.COMMAND, raider.getPos());
    }

}
