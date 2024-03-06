package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raid.class)
public abstract class MixinRaid {

    @Shadow @Final private ServerWorld world;

    @Inject(
            method = "addRaider",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
            )
    )
    public void onAddingRaiders(int wave, RaiderEntity raider, BlockPos pos, boolean existing, CallbackInfo ci) {
        ((InitializableMobEntity) raider).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.EVENT, raider.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(raider, world, SpawnReason.EVENT, raider.getPos());
    }

}
