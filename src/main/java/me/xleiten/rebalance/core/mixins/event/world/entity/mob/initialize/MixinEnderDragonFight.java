package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragonFight.class)
public abstract class MixinEnderDragonFight {

    @Shadow @Final private ServerWorld world;

    @Inject(
            method = "createDragon",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    public void onEnderDragonCreate(CallbackInfoReturnable<EnderDragonEntity> cir, @Local EnderDragonEntity enderDragonEntity) {
        ((InitializableMobEntity) enderDragonEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.MOB_SUMMONED, enderDragonEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(enderDragonEntity,  world, SpawnReason.MOB_SUMMONED, enderDragonEntity.getPos());
    }

}
