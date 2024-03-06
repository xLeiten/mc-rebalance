package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.util.math.DoubleRange;
import me.xleiten.rebalance.util.math.IntRange;
import me.xleiten.rebalance.util.math.Range;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.util.AttributeHelper.*;
import static me.xleiten.rebalance.util.math.RandomHelper.chance;
import static me.xleiten.rebalance.util.math.RandomHelper.range;

@Mixin(HuskEntity.class)
public abstract class MixinHuskEntity extends MixinZombieEntity
{
    @Unique private static final Option<DoubleRange> MOVE_SPEED_MULT = HUSK_SETTINGS.option("move-speed-mult", Range.create(0.35, 0.65));
    @Unique private static final Option<DoubleRange> BABY_MOVE_SPEED_MULT = HUSK_SETTINGS.option("baby-move-speed-mult", Range.create(-0.05, 0.2));
    @Unique private static final Option<DoubleRange> FOLLOW_RANGE_MULT = HUSK_SETTINGS.option("follow-range-mult", Range.create(2.0, 3.0));
    @Unique private static final Option<DoubleRange> ZOMBIE_REINFORCEMENT = HUSK_SETTINGS.option("reinforcement-attribute-value", Range.create(0.1, 0.6));
    @Unique private static final Option<Double> SPAWN_REINFORCEMENT_CHANCE = HUSK_SETTINGS.option("can-spawn-reinforcement-chance", 35d);
    @Unique private static final Option<Double> BREAK_DOORS_CHANCE = HUSK_SETTINGS.option("break-doors-chance", 30d);
    @Unique private static final Option<Boolean> SHOULD_ADAPT_TO_LIGHT = HUSK_SETTINGS.option("should-adapt-to-light", false);
    @Unique private static final Option<Boolean> SHOULD_BURN_IN_LIGHT = HUSK_SETTINGS.option("should-burn-in-light", false);
    @Unique private static final Option<Boolean> SHOULD_ALERT_OTHERS = HUSK_SETTINGS.option("should-alert-others", true);
    @Unique private static final Option<IntRange> ALERT_OTHERS_COOLDOWN = HUSK_SETTINGS.option("alert-others-cooldown", Range.create(80, 160));

    protected MixinHuskEntity(EntityType<? extends ZombieEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void cringeMod$onMobInitialize(ServerWorldAccess world, Random random, SpawnReason reason, Vec3d pos, Difficulty difficulty) {
        super.cringeMod$onMobInitialize(world, random, reason, pos, difficulty);

        this.cringeMod$setCanAdaptToLight(SHOULD_ADAPT_TO_LIGHT.getValue());
        this.cringeMod$setBurnInDayLight(SHOULD_BURN_IN_LIGHT.getValue());
        this.cringeMod$setCanAlertOthers(SHOULD_ALERT_OTHERS.getValue());

        addModifier(this, EntityAttributes.GENERIC_FOLLOW_RANGE, modifier("followRangeBoost", range(random, FOLLOW_RANGE_MULT.getValue())));
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", range(random, (isBaby() ? BABY_MOVE_SPEED_MULT : MOVE_SPEED_MULT).getValue())));

        if (chance(random, SPAWN_REINFORCEMENT_CHANCE.getValue()))
            setBaseValue(this, EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, range(random, ZOMBIE_REINFORCEMENT.getValue()));

        if (chance(random, BREAK_DOORS_CHANCE.getValue()))
            setCanBreakDoors(true);
    }

    @ModifyReturnValue(method = "burnsInDaylight", at = @At("RETURN"))
    public boolean burnsInDaylight(boolean original) {
        return cringeMod$canBurnInDayLight();
    }

    @Override
    protected void resetAlertCooldown() {
        this.alertOthersCooldown = range(random, ALERT_OTHERS_COOLDOWN.getValue());
    }
}
