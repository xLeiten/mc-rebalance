package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractPiglinEntity.class)
public abstract class MixinAbstractPiglinEntity {

    @Inject(
            method = "zombify",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ZombifiedPiglinEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z",
                    shift = At.Shift.AFTER
            )
    )
    public void onPiglinZombifyConversion(ServerWorld world, CallbackInfo ci, @Local ZombifiedPiglinEntity zombie) {
        ((InitializableMobEntity) zombie).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.CONVERSION, zombie.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(zombie, world, SpawnReason.CONVERSION, zombie.getPos());
    }

}
