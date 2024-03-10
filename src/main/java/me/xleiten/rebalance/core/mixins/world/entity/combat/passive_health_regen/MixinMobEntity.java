package me.xleiten.rebalance.core.mixins.world.entity.combat.passive_health_regen;

import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.world.entity.mob.Living;
import me.xleiten.rebalance.api.game.world.entity.tag.RebalanceEntityTypeTags;
import me.xleiten.rebalance.util.math.IntRange;
import me.xleiten.rebalance.util.math.RandomHelper;
import me.xleiten.rebalance.util.math.Range;
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
    @Unique private static final Option<IntRange> HEALTH_REGEN_COOLDOWN = Settings.PASSIVE_HEALTH_REGENERATION.option("healing-cooldown", Range.create(15, 35));
    @Unique private static final Option<Integer> HEALTH_REGEN_START_DELAY = Settings.PASSIVE_HEALTH_REGENERATION.option("after-damage-delay", 100);
    @Unique private static final Option<Float> HEALTH_REGEN_PERCENT = Settings.PASSIVE_HEALTH_REGENERATION.option("healing-percent-of-max-health", 0.05f);

    @Unique private int healthRegenCooldown = HEALTH_REGEN_COOLDOWN.getValue().min;
    @Unique private int afterDamageTicks = HEALTH_REGEN_START_DELAY.getValue();

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
                    healthRegenCooldown = RandomHelper.range(random, HEALTH_REGEN_COOLDOWN.getValue());
                    setHealth(health + maxHealth * HEALTH_REGEN_PERCENT.getValue());
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
            this.afterDamageTicks = HEALTH_REGEN_START_DELAY.getValue();
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
