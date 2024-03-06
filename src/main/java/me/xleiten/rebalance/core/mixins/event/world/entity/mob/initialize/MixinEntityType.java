package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(EntityType.class)
public abstract class MixinEntityType {

    @Inject(
            method = "create(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/NbtCompound;Ljava/util/function/Consumer;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/EntityData;",
                    shift = At.Shift.AFTER
            )
    )
    public void onEntityCreate(ServerWorld world, @Nullable NbtCompound itemNbt, @Nullable Consumer<Entity> afterConsumer, BlockPos pos, net.minecraft.entity.SpawnReason reason, boolean alignPosition, boolean invertY, CallbackInfoReturnable<@Nullable Entity> cir, @Local MobEntity mobEntity) {
        ((InitializableMobEntity) mobEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.from(reason), mobEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(mobEntity, world, SpawnReason.from(reason), mobEntity.getPos());
    }

}
