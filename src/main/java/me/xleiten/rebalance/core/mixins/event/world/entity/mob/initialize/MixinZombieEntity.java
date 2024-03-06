package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public abstract class MixinZombieEntity {

    @Inject(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/attribute/EntityAttributeInstance;addPersistentModifier(Lnet/minecraft/entity/attribute/EntityAttributeModifier;)V",
                    shift = At.Shift.AFTER,
                    ordinal = 1
            )
    )
    public void onZombieReinforcementSpawn(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir, @Local ZombieEntity zombie) {
        var world = (ServerWorld) zombie.getWorld();
        ((InitializableMobEntity) zombie).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.REINFORCEMENT, zombie.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(zombie, world, SpawnReason.REINFORCEMENT, zombie.getPos());
    }

    @Inject(
            method = "onKilledOther",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ZombieVillagerEntity;setXp(I)V",
                    shift = At.Shift.AFTER
            )
    )
    public void onZombieVillagerConversion(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir, @Local ZombieVillagerEntity zombie) {
        ((InitializableMobEntity) zombie).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.CONVERSION, zombie.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(zombie, world, SpawnReason.CONVERSION, zombie.getPos());
    }

    @Inject(
            method = "initialize",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/ServerWorldAccess;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    public void onChickenJockeySpawn(ServerWorldAccess world, LocalDifficulty difficulty, net.minecraft.entity.SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir, @Local ChickenEntity chickenEntity) {
        ((InitializableMobEntity) chickenEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.JOCKEY, chickenEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(chickenEntity, world, SpawnReason.JOCKEY, chickenEntity.getPos());
    }

    @ModifyReceiver(
            method = "convertTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ZombieEntity;setCanBreakDoors(Z)V"
            )
    )
    public ZombieEntity onZombieConversion(ZombieEntity instance, boolean canBreakDoors) {
        var world = (ServerWorld) instance.getWorld();
        ((InitializableMobEntity) instance).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.CONVERSION, instance.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(instance, world, SpawnReason.CONVERSION, instance.getPos());
        return instance;
    }

}
