package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SummonCommand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SummonCommand.class)
public abstract class MixinSummonCommand {

    @Inject(
            method = "summon",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/EntityData;",
                    shift = At.Shift.AFTER
            )
    )
    private static void onMobSummon(ServerCommandSource source, RegistryEntry.Reference<EntityType<?>> entityType, Vec3d pos, NbtCompound nbt, boolean initialize, CallbackInfoReturnable<Entity> cir, @Local Entity entity2) {
        var world = source.getWorld();
        ((InitializableMobEntity) entity2).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.COMMAND, entity2.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize((MobEntity) entity2, source.getWorld(), SpawnReason.COMMAND, entity2.getPos());
    }

}