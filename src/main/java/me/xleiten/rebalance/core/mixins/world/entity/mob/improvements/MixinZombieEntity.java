package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.xleiten.rebalance.api.game.world.entity.mob.Zombie;
import me.xleiten.rebalance.api.game.world.tag.RebalanceDamageTypeTags;
import me.xleiten.rebalance.core.game.world.entity.mob.ai.MobBlockBreakGoal;
import me.xleiten.rebalance.core.game.world.entity.mob.ai.MobBlockPlaceGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.api.math.RandomHelper.chance;
import static me.xleiten.rebalance.api.math.RandomHelper.range;
import static me.xleiten.rebalance.util.AttributeHelper.*;

@Mixin(ZombieEntity.class)
public abstract class MixinZombieEntity extends MixinHostileEntity implements Zombie
{
    @Shadow public abstract boolean isBaby();

    @Shadow public abstract void tick();

    @Shadow public abstract void setCanBreakDoors(boolean canBreakDoors);

    @Unique protected int alertOthersCooldown = 40;
    @Unique protected boolean shouldAdaptToLight = MOB_ZOMBIE__SHOULD_ADAPT_TO_LIGHT.value();
    @Unique protected boolean shouldBurnInDayLight = MOB_ZOMBIE__SHOULD_BURN_IN_LIGHT.value();
    @Unique protected boolean shouldAlertOthers = MOB_ZOMBIE__SHOULD_ALERT_OTHERS.value();
    @Unique protected int spawnReinforcementCooldown = MOB_ZOMBIE__REINFORCEMENT_COOLDOWN.value();

    @Unique protected final TargetPredicate TARGET_PREDICATE = TargetPredicate.createAttackable().ignoreVisibility().setPredicate(entity -> !(entity instanceof PlayerEntity player) || !player.getAbilities().creativeMode);

    protected MixinZombieEntity(EntityType<? extends HostileEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason reason) {
        super.rebalanceMod$onFirstSpawn(world, random, reason);
        if (this.getType() == EntityType.ZOMBIE) {
            addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", range(random, (isBaby() ? MOB_ZOMBIE__BABY_MOVE_SPEED_MULT : MOB_ZOMBIE__MOVE_SPEED_MULT).value())));
            if (chance(random, MOB_ZOMBIE__SPAWN_REINFORCEMENT_CHANCE.value()))
                setBaseValue(this, EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, range(random, MOB_ZOMBIE__ZOMBIE_REINFORCEMENT.value()));

            addModifier(this, EntityAttributes.GENERIC_FOLLOW_RANGE, modifier("followRangeBoost", range(random, MOB_ZOMBIE__FOLLOW_RANGE_MULT.value())));
            if (chance(random, MOB_ZOMBIE__BREAK_DOORS_CHANCE.value()))
                setCanBreakDoors(true);

            if (chance(random, MOB_ZOMBIE__SPAWN_ADAPTED_TO_LIGHT_CHANCE.value())) {
                shouldBurnInDayLight = false;
                shouldAdaptToLight = false;
            }
        }
    }

    @ModifyArgs(
            method = "initCustomGoals",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V",
                    ordinal = 4
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
                    ordinal = 6
            )
    )
    protected void modifyIronGolemTargetGoal(Args args) {
        args.set(0, 2);
        args.set(1, new ActiveTargetGoal<>((ZombieEntity) (Object) this, GolemEntity.class, false));
    }

    @ModifyArgs(
            method = "initCustomGoals",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V",
                    ordinal = 5
            )
    )
    protected void modifyMerchantTargetGoal(Args args) {
        args.set(0, 3);
        args.set(1, new ActiveTargetGoal<>((ZombieEntity) (Object) this, MerchantEntity.class, false));
    }

    @Inject(
            method = "initCustomGoals",
            at = @At("HEAD")
    )
    protected void onCustomGoalsSetup(CallbackInfo ci) {
        goalSelector.add(3, new MobBlockBreakGoal((ZombieEntity) (Object) this));
        goalSelector.add(3, new MobBlockPlaceGoal((ZombieEntity) (Object) this));
    }

    @Override
    public void cringeMod$onCustomNbtDataRead(NbtCompound nbt) {
        super.cringeMod$onCustomNbtDataRead(nbt);
        if (nbt.contains("ShouldBurnInLight")) shouldBurnInDayLight = nbt.getBoolean("ShouldBurnInLight");
        if (nbt.contains("ShouldAdaptToLight")) shouldAdaptToLight = nbt.getBoolean("ShouldAdaptToLight");
        if (nbt.contains("ShouldAlertOthers")) shouldAlertOthers = nbt.getBoolean("ShouldAlertOthers");
    }

    @Override
    public void cringeMod$onCustomNbtDataWrite(NbtCompound nbt) {
        super.cringeMod$onCustomNbtDataWrite(nbt);
        nbt.putBoolean("ShouldBurnInLight", shouldBurnInDayLight);
        nbt.putBoolean("ShouldAdaptToLight", shouldAdaptToLight);
        nbt.putBoolean("ShouldAlertOthers", shouldAlertOthers);
    }

    /**
     * @author xLeiten
     * @reason cringe
     */
    @Overwrite
    public void initEquipment(Random random, LocalDifficulty localDifficulty) {
        super.initEquipment(random, localDifficulty);
        if (chance(random, this.getWorld().getDifficulty() == Difficulty.HARD ? MOB_ZOMBIE__SPAWN_WITH_TOOL_CHANCE.value() : 1F)) {
            Item mainTool;
            if (chance(random, 50))
                mainTool = Items.IRON_SWORD;
            else {
                if (getY() <= 40) {
                    mainTool = chance(random, 70) ? Items.IRON_PICKAXE : Items.IRON_SHOVEL;
                } else
                    mainTool = chance(random, 50) ? Items.IRON_AXE : Items.IRON_SHOVEL;
            }
            equipStack(EquipmentSlot.MAINHAND, new ItemStack(mainTool));
        }
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ZombieEntity;isConvertingInWater()Z",
                    shift = At.Shift.BEFORE
            )
    )
    protected void onMovementTick(CallbackInfo ci) {
        if (cringeMod$canAlertOthers()) {
            if (alertOthersCooldown-- <= 0) {
                alertOthers();
                resetAlertCooldown();
            }
        }
    }

    @ModifyExpressionValue(
            method = "tickMovement",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/ZombieEntity;isAffectedByDaylight()Z")
    )
    protected boolean checkLightAdaptation(boolean original) {
        return original && !tryToAdaptToLight();
    }

    @ModifyReturnValue(
            method = "burnsInDaylight",
            at = @At("RETURN")
    )
    protected boolean burnsInDaylight(boolean original) {
        return shouldBurnInDayLight;
    }

    @ModifyReceiver(
            method = "convertTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ZombieEntity;setCanBreakDoors(Z)V"
            )
    )
    protected net.minecraft.entity.mob.ZombieEntity saveHealthOnConvert(net.minecraft.entity.mob.ZombieEntity instance, boolean canBreakDoors) {
        instance.setHealth(this.getHealth());
        return instance;
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ZombieEntity;isConvertingInWater()Z"
            )
    )
    protected void tickReinforcementCooldown(CallbackInfo ci) {
        if (spawnReinforcementCooldown > 0) {
            spawnReinforcementCooldown--;
        }
    }

    @Inject(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ZombieEntity;getWorld()Lnet/minecraft/world/World;",
                    ordinal = 1
            ),
            cancellable = true
    )
    protected void changeReinforcementRequirements(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (spawnReinforcementCooldown > 0 || !source.isIn(RebalanceDamageTypeTags.SUMMONS_ZOMBIE_REINFORCEMENT)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ZombieEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;",
                    shift = At.Shift.AFTER
            )
    )
    protected void resetReinforcementCooldown(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        spawnReinforcementCooldown = MOB_ZOMBIE__REINFORCEMENT_COOLDOWN.value();
    }

    // interface methods

    @Override
    public void cringeMod$onEntityAddedToWorld(ServerWorld world, Vec3d pos) {
        super.cringeMod$onEntityAddedToWorld(world, pos);
        searchForTarget();
    }

    @Override
    public void cringeMod$setCanAlertOthers(boolean shouldAlert) {
        this.shouldAlertOthers = shouldAlert;
    }

    @Override
    public boolean cringeMod$canAlertOthers() {
        return shouldAlertOthers;
    }

    @Override
    public void cringeMod$setBurnInDayLight(boolean shouldBurn) {
        this.shouldBurnInDayLight = shouldBurn;
    }

    @Override
    public boolean cringeMod$canBurnInDayLight() {
        return shouldBurnInDayLight;
    }

    @Override
    public void cringeMod$setCanAdaptToLight(boolean canAdapt) {
        this.shouldAdaptToLight = canAdapt;
    }

    @Override
    public boolean cringeMod$canAdaptToLight() {
        return shouldAdaptToLight;
    }


    // custom methods

    @Unique
    protected void searchForTarget() {
        if (this.getTarget() != null || !chance(random, MOB_ZOMBIE__SEARCH_TARGET_ON_SPAWN_CHANCE.value())) return;
        var player = getWorld().getClosestPlayer(TARGET_PREDICATE.setBaseMaxDistance(getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE) * 0.7), this);
        if (player != null) {
            this.setTarget(player);
        }
    }

    @Unique
    protected void alertOthers() {
        var target = getTarget();
        if (target == null || isSilent()) return;
        var followRange = getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE) * 0.7;
        var zombies = getWorld().getEntitiesByClass(net.minecraft.entity.mob.ZombieEntity.class, Box.of(getPos(), followRange, followRange / 1.5, followRange), mob -> mob.getTarget() == null && !(mob instanceof ZombifiedPiglinEntity) && !mob.isSilent());
        var chance = MOB_ZOMBIE__ALERT_OTHERS_CHANCE.value() + zombies.size() * 3;
        for (net.minecraft.entity.mob.ZombieEntity zombie : zombies) {
            if (chance(random, chance) && TARGET_PREDICATE.setBaseMaxDistance(followRange).test(zombie, target)) {
                zombie.setTarget(target);
            }
        }
    }

    @Unique
    protected boolean tryToAdaptToLight() {
        if (shouldAdaptToLight) {
            if (chance(random, MOB_ZOMBIE__LIGHT_ADAPTATION_CHANCE.value())) {
                this.extinguish();
                shouldBurnInDayLight = false;
                shouldAdaptToLight = false;
                return true;
            }
        }
        return false;
    }

    @Unique
    protected void resetAlertCooldown() {
        this.alertOthersCooldown = range(random, MOB_ZOMBIE__ALERT_OTHERS_COOLDOWN.value());
    }
}