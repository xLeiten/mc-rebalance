package me.xleiten.rebalance.core.mixins.world.entity.combat.health_slowdown;

import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.world.entity.mob.attribute.AttributeModifier;
import me.xleiten.rebalance.api.game.world.entity.mob.Living;
import me.xleiten.rebalance.api.game.world.entity.tags.ModEntityTypeTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
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
    @Unique private static final Option<Float> HEALTH_SLOWDOWN_STOP_LIMIT = Settings.MOB_SETTINGS.option("health-slowdown-stop-limit", 0.6f);
    @Unique private static final Option<Float> HEALTH_RATIO_MULTIPLIER = Settings.PLAYER_SETTINGS.option("health-slowdown-ratio-mult", 0.8f);

    @Shadow public abstract float getHealth();
    @Shadow public abstract float getMaxHealth();
    @Shadow public abstract boolean isAlive();
    @Shadow @Nullable public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

    @Unique private static final UUID HEALTH_SLOWDOWN_UUID = UUID.fromString("e7c316a0-7f28-4897-a096-57e572e66ce3");

    protected MixinLivingEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Inject(
            method = "setHealth",
            at = @At("TAIL")
    )
    public void recheckHealth(CallbackInfo ci) {
        if (isAlive() && !getType().isIn(ModEntityTypeTags.NOT_AFFECTED_BY_HEALTH_SLOWDOWN)) {
            var moveSpeedAttribute = getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            if (moveSpeedAttribute != null) {
                var modifier = moveSpeedAttribute.getModifier(HEALTH_SLOWDOWN_UUID);
                var ratio = getHealth() / getMaxHealth();
                if (modifier != null) {
                    ((AttributeModifier) modifier).cringeMod$setValue(ratio < getHealthSlowdownStopLimit() ? calcHealthSlowdownValue(ratio) : 0);
                    moveSpeedAttribute.onUpdate();
                } else {
                    moveSpeedAttribute.addPersistentModifier(
                            new EntityAttributeModifier(HEALTH_SLOWDOWN_UUID, "healthSlowDown", calcHealthSlowdownValue(ratio), MULTIPLY_BASE)
                    );
                }
            }
        }
    }

    @Unique
    private double calcHealthSlowdownValue(float ratio) {
        var temp = 1 - ratio;
        return -temp * Math.sqrt(temp) * getHealthRatioMult();
    }

    @Unique
    public float getHealthSlowdownStopLimit() {
        return HEALTH_SLOWDOWN_STOP_LIMIT.getValue();
    }

    @Unique
    public float getHealthRatioMult() {
        return HEALTH_RATIO_MULTIPLIER.getValue();
    }
}
