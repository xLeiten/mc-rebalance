package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs;

import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.util.math.DoubleRange;
import me.xleiten.rebalance.util.math.IntRange;
import me.xleiten.rebalance.util.math.Range;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.util.AttributeHelper.*;
import static me.xleiten.rebalance.util.math.RandomHelper.chance;
import static me.xleiten.rebalance.util.math.RandomHelper.range;

@Mixin(ZombieVillagerEntity.class)
public abstract class MixinZombieVillager extends MixinZombieEntity
{
    @Unique private static final Option<DoubleRange> MOVE_SPEED_MULT = ZOMBIE_VILLAGER_SETTINGS.option("move-speed-mult", Range.create(0.35, 0.55));
    @Unique private static final Option<DoubleRange> BABY_MOVE_SPEED_MULT = ZOMBIE_VILLAGER_SETTINGS.option("baby-move-speed-mult", Range.create(-0.05, 0.2));
    @Unique private static final Option<DoubleRange> FOLLOW_RANGE_MULT = ZOMBIE_VILLAGER_SETTINGS.option("follow-range-mult", Range.create(2.0, 3.0));
    @Unique private static final Option<Double> BREAK_DOORS_CHANCE = ZOMBIE_VILLAGER_SETTINGS.option("break-doors-chance", 55d);
    @Unique private static final Option<Boolean> SHOULD_ADAPT_TO_LIGHT = ZOMBIE_VILLAGER_SETTINGS.option("should-adapt-to-light", true);
    @Unique private static final Option<Boolean> SHOULD_BURN_IN_LIGHT = ZOMBIE_VILLAGER_SETTINGS.option("should-burn-in-light", true);
    @Unique private static final Option<Boolean> SHOULD_ALERT_OTHERS = ZOMBIE_VILLAGER_SETTINGS.option("should-alert-others", true);
    @Unique private static final Option<Integer> SPAWN_ADAPTED_TO_LIGHT_CHANCE = ZOMBIE_VILLAGER_SETTINGS.option("spawn-with-light-adaptation-chance", 3);
    @Unique private static final Option<IntRange> ALERT_OTHERS_COOLDOWN = ZOMBIE_VILLAGER_SETTINGS.option("alert-others-cooldown", Range.create(80, 160));

    protected MixinZombieVillager(EntityType<? extends ZombieVillagerEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void cringeMod$onMobInitialize(ServerWorldAccess world, Random random, SpawnReason reason, Vec3d pos, Difficulty difficulty) {
        super.cringeMod$onMobInitialize(world, random, reason, pos, difficulty);

        shouldAdaptToLight = SHOULD_ADAPT_TO_LIGHT.getValue();
        shouldBurnInDayLight = SHOULD_BURN_IN_LIGHT.getValue();
        shouldAlertOthers = SHOULD_ALERT_OTHERS.getValue();

        addModifier(this, EntityAttributes.GENERIC_FOLLOW_RANGE, modifier("followRangeBoost", range(random, FOLLOW_RANGE_MULT.getValue())));
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", range(random, (isBaby() ? BABY_MOVE_SPEED_MULT : MOVE_SPEED_MULT).getValue())));

        if (chance(random, BREAK_DOORS_CHANCE.getValue()))
            setCanBreakDoors(true);

        if (chance(random, SPAWN_ADAPTED_TO_LIGHT_CHANCE.getValue())) {
            shouldBurnInDayLight = false;
            shouldAdaptToLight = false;
        }

        setBaseValue(this, EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, 0);
    }

    @Override
    protected boolean burnsInDaylight(boolean original) {
        return cringeMod$canBurnInDayLight();
    }

    @Override
    protected void resetAlertCooldown() {
        this.alertOthersCooldown = range(random, ALERT_OTHERS_COOLDOWN.getValue());
    }
}
