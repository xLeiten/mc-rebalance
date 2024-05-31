package me.xleiten.rebalance.core.mixins.world.entity.health_slowdown;

import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.game.world.entity.mob.Living;
import me.xleiten.rebalance.api.game.world.tag.RebalanceEntityTypeTags;
import me.xleiten.rebalance.util.AttributeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements Living
{
    @Shadow public abstract float getHealth();
    @Shadow public abstract float getMaxHealth();
    @Shadow public abstract boolean isAlive();

    @Shadow @Nullable public abstract EntityAttributeInstance getAttributeInstance(RegistryEntry<EntityAttribute> attribute);

    @Unique private static final UUID MODIFIER_UUID = UUID.fromString(Settings.SLOWDOWN_BY_HEALTH__MOVE_SPEED_MODIFIER_UUID.value());

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
            AttributeHelper.addModifier(getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                    AttributeHelper.modifier("healthSlowdown", ratio < getAffectionHealthLimit() ? calcSlowdownValue(ratio) : 0)
                            .id(MODIFIER_UUID)
                            .overwrite(true)
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
        return Settings.SLOWDOWN_BY_HEALTH__AFFECTION_HEALTH_LIMIT.value();
    }

    @Unique
    public float getEffectMultiplier() {
        return Settings.SLOWDOWN_BY_HEALTH__EFFECT_MULTIPLIER.value();
    }
}
