package me.xleiten.rebalance.core.mixins.world.entity.mob.passive_health_regen;

import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.game.world.entity.mob.Living;
import me.xleiten.rebalance.api.game.world.tag.RebalanceEntityTypeTags;
import me.xleiten.rebalance.api.math.RandomHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MixinMobEntity extends LivingEntity implements Living
{
    @Unique private int healthRegenCooldown = Settings.PASSIVE_HEALTH_REGENERATION__COOLDOWN.value().min;
    @Unique private int afterDamageTicks = Settings.PASSIVE_HEALTH_REGENERATION__START_DELAY.value();

    protected MixinMobEntity(EntityType<? extends MobEntity> type, World world)
    {
        super(type, world);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    public void tryRegenHealth(CallbackInfo ci) {
        if (isAlive() && cringeMod$canRegenHealth()) {
            var health = getHealth();
            var maxHealth = getMaxHealth();
            if (health < maxHealth) {
                if (healthRegenCooldown-- <= 0) {
                    healthRegenCooldown = RandomHelper.range(random, Settings.PASSIVE_HEALTH_REGENERATION__COOLDOWN.value());
                    setHealth(health + maxHealth * Settings.PASSIVE_HEALTH_REGENERATION__HEALTH_PERCENT.value());
                }
            }
        }
        if (afterDamageTicks > 0)
            afterDamageTicks--;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        var result = super.damage(source, amount);
        if (isAlive()) {
            this.afterDamageTicks = Settings.PASSIVE_HEALTH_REGENERATION__START_DELAY.value();
        }
        return result;
    }

    @Override
    public int cringeMod$getHealthRegenCooldown() {
        return healthRegenCooldown;
    }

    @Override
    public void cringeMod$setHealthRegenCooldown(int cooldown) {
        this.healthRegenCooldown = cooldown > 0 ? cooldown : 1;
    }

    @Override
    public boolean cringeMod$canRegenHealth() {
        return afterDamageTicks <= 0 && !getType().isIn(RebalanceEntityTypeTags.CANNOT_REGEN_HEALTH);
    }
}
