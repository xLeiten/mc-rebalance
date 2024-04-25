package me.xleiten.rebalance.core.mixins.world.entity.health_slowdown;

import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.world.entity.mob.Living;
import me.xleiten.rebalance.api.game.world.entity.tag.RebalanceEntityTypeTags;
import me.xleiten.rebalance.util.AttributeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

import static net.minecraft.entity.attribute.EntityAttributeModifier.Operation.MULTIPLY_BASE;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements Living
{
    @Unique private static final Option<Float> AFFECTION_HEALTH_LIMIT = Settings.SLOWDOWN_BY_HEALTH.option("affection-health-limit", 0.6f);
    @Unique private static final Option<Float> EFFECT_MULTIPLIER = Settings.SLOWDOWN_BY_HEALTH.option("effect-multiplier", 0.8f);
    @Unique private static final Option<String> MOVE_SPEED_MODIFIER_UUID = Settings.SLOWDOWN_BY_HEALTH.option("modifier-uuid", "e7c316a0-7f28-4897-a096-57e572e66ce3");

    @Shadow public abstract float getHealth();
    @Shadow public abstract float getMaxHealth();
    @Shadow public abstract boolean isAlive();
    @Shadow @Nullable public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

    @Unique private static final UUID MODIFIER_UUID = UUID.fromString(MOVE_SPEED_MODIFIER_UUID.getValue());

    protected MixinLivingEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Inject(
            method = "setHealth",
            at = @At("TAIL")
    )
    public void recheckHealth(CallbackInfo ci) {
        if (isAlive() && !getType().isIn(RebalanceEntityTypeTags.NOT_AFFECTED_BY_HEALTH_SLOWDOWN)) {
            var ratio = getHealth() / getMaxHealth();
            AttributeHelper.addOrSetModifier(
                    getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                    MODIFIER_UUID,
                    ratio < getAffectionHealthLimit() ? calcSlowdownValue(ratio) : 0,
                    "healthSlowdown",
                    MULTIPLY_BASE,
                    true
            );
        }
    }

    @Unique
    private double calcSlowdownValue(float healthRatio) {
        var temp = 1 - healthRatio;
        return -temp * Math.sqrt(temp) * getEffectMultiplier();
    }

    @Unique
    public float getAffectionHealthLimit() {
        return AFFECTION_HEALTH_LIMIT.getValue();
    }

    @Unique
    public float getEffectMultiplier() {
        return EFFECT_MULTIPLIER.getValue();
    }
}
