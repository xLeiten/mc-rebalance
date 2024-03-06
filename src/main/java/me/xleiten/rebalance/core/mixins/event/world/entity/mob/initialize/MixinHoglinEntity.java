package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.ZoglinEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HoglinEntity.class)
public abstract class MixinHoglinEntity {

    @Inject(
            method = "zombify",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ZoglinEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z",
                    shift = At.Shift.AFTER
            )
    )
    public void onHoglinZombify(ServerWorld world, CallbackInfo ci, @Local ZoglinEntity zombie) {
        ((InitializableMobEntity) zombie).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.CONVERSION, zombie.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(zombie, world, SpawnReason.CONVERSION, zombie.getPos());
    }

}
