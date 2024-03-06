package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieVillagerEntity.class)
public abstract class MixinZombieVillagerEntity {

    @Inject(
            method = "finishConversion",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/passive/VillagerEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z",
                    shift = At.Shift.AFTER
            )
    )
    public void onVillagerConversionBack(ServerWorld world, CallbackInfo ci, @Local VillagerEntity villagerEntity) {
        ((InitializableMobEntity) villagerEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.CONVERSION, villagerEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(villagerEntity, world, SpawnReason.CONVERSION, villagerEntity.getPos());
    }

}
