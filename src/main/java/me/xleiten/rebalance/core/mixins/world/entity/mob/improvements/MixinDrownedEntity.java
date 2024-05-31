package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.api.math.RandomHelper.chance;
import static me.xleiten.rebalance.api.math.RandomHelper.range;
import static me.xleiten.rebalance.util.AttributeHelper.*;

@Mixin(DrownedEntity.class)
public abstract class MixinDrownedEntity extends MixinZombieEntity
{
    protected MixinDrownedEntity(EntityType<? extends HostileEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void initCustomVars(EntityType<? extends ZombieEntity> entityType, World world, CallbackInfo ci) {
        this.cringeMod$setCanAdaptToLight(MOB_DROWNED__SHOULD_ADAPT_TO_LIGHT.value());
        this.cringeMod$setBurnInDayLight(MOB_DROWNED__SHOULD_BURN_IN_LIGHT.value());
        this.cringeMod$setCanAlertOthers(MOB_DROWNED__SHOULD_ALERT_OTHERS.value());
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason) {
        super.rebalanceMod$onFirstSpawn(world, random, reason);

        addModifier(this, EntityAttributes.GENERIC_FOLLOW_RANGE, modifier("followRangeBoost", range(random, MOB_DROWNED__FOLLOW_RANGE_MULT.value())));
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", range(random, (isBaby() ? MOB_DROWNED__BABY_MOVE_SPEED_MULT : MOB_DROWNED__MOVE_SPEED_MULT).value())));

        if (chance(random, MOB_DROWNED__BREAK_DOORS_CHANCE.value()))
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
        this.alertOthersCooldown = range(random, MOB_DROWNED__ALERT_OTHERS_COOLDOWN.value());
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
