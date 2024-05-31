package me.xleiten.rebalance.core.mixins.world.entity.mob.health_display;

import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.game.world.entity.HealthDisplay;
import me.xleiten.rebalance.api.game.world.entity.mob.Living;
import me.xleiten.rebalance.api.game.world.tag.RebalanceEntityTypeTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements Living
{
    @Unique private final HealthDisplay healthDisplay = new HealthDisplay((LivingEntity) (Object) this);

    protected MixinLivingEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    public void onTick(CallbackInfo ci) {
        healthDisplay.tick();
    }

    @Inject(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;isDead()Z"
            )
    )
    public void showHealth(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() instanceof PlayerEntity && !getType().isIn(RebalanceEntityTypeTags.WITHOUT_HEALTH_DISPLAY))
            healthDisplay.show(Settings.HEALTH_DISPLAY__DISPLAY_TICKS.value());
    }

    @Override
    public @NotNull HealthDisplay cringeMod$getHealthDisplay() {
        return healthDisplay;
    }

    @Nullable
    @Override
    public Text getCustomName() {
        return healthDisplay.isActive() ? healthDisplay.getSavedCustomName() : super.getCustomName();
    }

    @Override
    public boolean isCustomNameVisible() {
        return healthDisplay.isActive() ? healthDisplay.hasVisibleCustomName() : super.isCustomNameVisible();
    }
}
