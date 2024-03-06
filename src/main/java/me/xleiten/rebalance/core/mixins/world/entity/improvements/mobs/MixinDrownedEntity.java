package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs;

import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.util.math.DoubleRange;
import me.xleiten.rebalance.util.math.IntRange;
import me.xleiten.rebalance.util.math.Range;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.util.AttributeHelper.*;
import static me.xleiten.rebalance.util.math.RandomHelper.chance;
import static me.xleiten.rebalance.util.math.RandomHelper.range;

@Mixin(DrownedEntity.class)
public abstract class MixinDrownedEntity extends MixinZombieEntity
{
    @Unique private static final Option<DoubleRange> MOVE_SPEED_MULT = DROWNED_SETTINGS.option("move-speed-mult", Range.create(0.15, 0.45));
    @Unique private static final Option<DoubleRange> BABY_MOVE_SPEED_MULT = DROWNED_SETTINGS.option("baby-move-speed-mult", Range.create(-0.05, 0.2));
    @Unique private static final Option<DoubleRange> FOLLOW_RANGE_MULT = DROWNED_SETTINGS.option("follow-range-mult", Range.create(2.0, 3.0));
    @Unique private static final Option<Double> BREAK_DOORS_CHANCE = DROWNED_SETTINGS.option("break-doors-chance", 20d);
    @Unique private static final Option<Boolean> SHOULD_ADAPT_TO_LIGHT = DROWNED_SETTINGS.option("should-adapt-to-light", false);
    @Unique private static final Option<Boolean> SHOULD_BURN_IN_LIGHT = DROWNED_SETTINGS.option("should-burn-in-light", true);
    @Unique private static final Option<Boolean> SHOULD_ALERT_OTHERS = DROWNED_SETTINGS.option("should-alert-others", true);
    @Unique private static final Option<IntRange> ALERT_OTHERS_COOLDOWN = DROWNED_SETTINGS.option("alert-others-cooldown", Range.create(80, 160));

    protected MixinDrownedEntity(EntityType<? extends HostileEntity> entityType, World world)
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

        if (chance(random, BREAK_DOORS_CHANCE.getValue()))
            setCanBreakDoors(true);
    }

    @Override
    protected void searchForTarget() {
        if (this.getTarget() != null) return;
        var followRange = getValue(this, EntityAttributes.GENERIC_FOLLOW_RANGE, 35);
        var player = getWorld().getClosestPlayer(
                TargetPredicate.DEFAULT.setBaseMaxDistance(followRange),
                this
        );
        if (player != null && player.isSubmergedInWater())
            setTarget(player);
    }

    @Override
    protected void alertOthers() {
        if (getTarget() == null || !getTarget().isSubmergedInWater()) return;
        super.alertOthers();
    }

    @Override
    protected void resetAlertCooldown() {
        this.alertOthersCooldown = range(random, ALERT_OTHERS_COOLDOWN.getValue());
    }

    @ModifyArgs(
            method = "initCustomGoals",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V",
                    ordinal = 7
            )
    )
    protected void modifyPlayerTargetGoal(Args args) {
        args.set(0, 3);
        args.set(1, new ActiveTargetGoal<>((ZombieEntity) (Object) this, PlayerEntity.class, false));
    }

    @ModifyArgs(
            method = "initCustomGoals",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V",
                    ordinal = 9
            )
    )
    protected void modifyIronGolemTargetGoal(Args args) {
        args.set(0, 2);
        args.set(1, new ActiveTargetGoal<>((ZombieEntity) (Object) this, GolemEntity.class, false));
    }
}
