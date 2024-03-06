package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StriderEntity.class)
public abstract class MixinStriderEntity {

    @Inject(
            method = "initialize",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V",
                    shift = At.Shift.AFTER
            )
    )
    public void onJockeySpawn(ServerWorldAccess world, LocalDifficulty difficulty, net.minecraft.entity.SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir, @Local MobEntity mobEntity) {
        ((InitializableMobEntity) mobEntity).cringeMod$onMobInitialize(world, world.getRandom(), me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason.JOCKEY, mobEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(mobEntity, world, me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason.JOCKEY, mobEntity.getPos());
    }

    @Inject(
            method = "initialize",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/passive/StriderEntity;initializeRider(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;",
                    shift = At.Shift.AFTER
            )
    )
    public void onAnotherStriderSpawn(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir, @Local PassiveEntity passiveEntity) {
        ((InitializableMobEntity) passiveEntity).cringeMod$onMobInitialize(world, world.getRandom(), me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason.JOCKEY, passiveEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(passiveEntity, world, me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason.JOCKEY, passiveEntity.getPos());
    }

}
