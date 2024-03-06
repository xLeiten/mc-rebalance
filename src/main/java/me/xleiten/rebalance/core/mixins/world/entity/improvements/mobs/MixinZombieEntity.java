package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.core.game.world.entity.mob.ai.HostileEntityPlaceBlockGoal;
import me.xleiten.rebalance.core.game.world.entity.mob.ai.HostileEntityBlockBreakGoal;
import me.xleiten.rebalance.api.game.world.entity.mob.Zombie;
import me.xleiten.rebalance.util.AttributeHelper;
import me.xleiten.rebalance.util.math.DoubleRange;
import me.xleiten.rebalance.util.math.IntRange;
import me.xleiten.rebalance.util.math.Range;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static me.xleiten.rebalance.Settings.ZOMBIE_SETTINGS;
import static me.xleiten.rebalance.util.AttributeHelper.*;
import static me.xleiten.rebalance.util.math.RandomHelper.*;

@Mixin(net.minecraft.entity.mob.ZombieEntity.class)
public abstract class MixinZombieEntity extends MixinHostileEntity implements Zombie
{
    @Unique private static final Option<DoubleRange> MOVE_SPEED_MULT = ZOMBIE_SETTINGS.option("move-speed-mult", Range.create(0.35, 0.65));
    @Unique private static final Option<DoubleRange> BABY_MOVE_SPEED_MULT = ZOMBIE_SETTINGS.option("baby-move-speed-mult", Range.create(-0.05, 0.2));
    @Unique private static final Option<DoubleRange> FOLLOW_RANGE_MULT = ZOMBIE_SETTINGS.option("follow-range-mult", Range.create(2.0, 3.0));
    @Unique private static final Option<DoubleRange> ZOMBIE_REINFORCEMENT = ZOMBIE_SETTINGS.option("reinforcement-attribute-value", Range.create(0.1, 0.6));
    @Unique private static final Option<Double> SPAWN_REINFORCEMENT_CHANCE = ZOMBIE_SETTINGS.option("can-spawn-reinforcement-chance", 40d);
    @Unique private static final Option<Integer> REINFORCEMENT_COOLDOWN = ZOMBIE_SETTINGS.option("reinforcement-cooldown", 100);
    @Unique private static final Option<IntRange> ALERT_OTHERS_COOLDOWN = ZOMBIE_SETTINGS.option("alert-others-cooldown", Range.create(80, 160));
    @Unique private static final Option<Double> ALERT_OTHERS_CHANCE = ZOMBIE_SETTINGS.option("alert-others-chance", 50d);
    @Unique private static final Option<Double> SEARCH_TARGET_ON_SPAWN_CHANCE = ZOMBIE_SETTINGS.option("search-target-on-spawn-chance", 70d);
    @Unique private static final Option<Integer> SPAWN_ADAPTED_TO_LIGHT_CHANCE = ZOMBIE_SETTINGS.option("spawn-with-light-adaptation-chance", 3);
    @Unique private static final Option<Double> LIGHT_ADAPTATION_CHANCE = ZOMBIE_SETTINGS.option("light-adaptation-chance", 3d);
    @Unique private static final Option<Double> BREAK_DOORS_CHANCE = ZOMBIE_SETTINGS.option("break-doors-chance", 35d);
    @Unique private static final Option<Double> SPAWN_WITH_TOOL_CHANCE = ZOMBIE_SETTINGS.option("spawn-with-tool-chance", 15d);
    @Unique private static final Option<Boolean> SHOULD_BURN_IN_LIGHT = ZOMBIE_SETTINGS.option("should-burn-in-light", true);
    @Unique private static final Option<Boolean> SHOULD_ADAPT_TO_LIGHT = ZOMBIE_SETTINGS.option("should-adapt-to-light", true);
    @Unique private static final Option<Boolean> SHOULD_ALERT_OTHERS = ZOMBIE_SETTINGS.option("should-alert-others", true);

    @Shadow public abstract boolean isBaby();

    @Shadow public abstract void tick();

    @Shadow public abstract void setCanBreakDoors(boolean canBreakDoors);

    @Unique protected int alertOthersCooldown = 40;
    @Unique protected boolean shouldAdaptToLight = SHOULD_ADAPT_TO_LIGHT.getValue();
    @Unique protected boolean shouldBurnInDayLight = SHOULD_BURN_IN_LIGHT.getValue();
    @Unique protected boolean shouldAlertOthers = SHOULD_ALERT_OTHERS.getValue();
    @Unique protected int spawnReinforcementCooldown = REINFORCEMENT_COOLDOWN.getValue();

    @Unique protected final TargetPredicate onSpawnTargetPredicate = TargetPredicate.createAttackable().ignoreVisibility().setPredicate(entity -> !((PlayerEntity) entity).getAbilities().creativeMode);

    protected MixinZombieEntity(EntityType<? extends HostileEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void cringeMod$onMobInitialize(ServerWorldAccess world, Random random, me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason reason, Vec3d pos, Difficulty difficulty) {
        super.cringeMod$onMobInitialize(world, random, reason, pos, difficulty);
        if (this.getType() == EntityType.ZOMBIE) {
            addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", range(random, (isBaby() ? BABY_MOVE_SPEED_MULT : MOVE_SPEED_MULT).getValue())));
            if (chance(random, SPAWN_REINFORCEMENT_CHANCE.getValue()))
                setBaseValue(this, EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, range(random, ZOMBIE_REINFORCEMENT.getValue()));

            addModifier(this, EntityAttributes.GENERIC_FOLLOW_RANGE, modifier("followRangeBoost", range(random, FOLLOW_RANGE_MULT.getValue())));
            if (chance(random, BREAK_DOORS_CHANCE.getValue()))
                setCanBreakDoors(true);

            if (chance(random, SPAWN_ADAPTED_TO_LIGHT_CHANCE.getValue())) {
                shouldBurnInDayLight = false;
                shouldAdaptToLight = false;
            }
        }
    }

    // injectors

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
        goalSelector.add(3, new HostileEntityBlockBreakGoal((net.minecraft.entity.mob.ZombieEntity) (Object) this));
        goalSelector.add(3, new HostileEntityPlaceBlockGoal((net.minecraft.entity.mob.ZombieEntity) (Object) this));
    }

    @Override
    public void cringeMod$onCustomNbtDataRead(NbtCompound nbt) {
        super.cringeMod$onCustomNbtDataRead(nbt);
        shouldBurnInDayLight = nbt.getBoolean("ShouldBurnInLight");
        shouldAdaptToLight = nbt.getBoolean("ShouldAdaptToLight");
        shouldAlertOthers = nbt.getBoolean("ShouldAlertOthers");
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
     * @reason don't like it
     */
    @Overwrite
    public void initEquipment(Random random, LocalDifficulty localDifficulty) {
        super.initEquipment(random, localDifficulty);
        if (chance(random, this.getWorld().getDifficulty() == Difficulty.HARD ? SPAWN_WITH_TOOL_CHANCE.getValue() : 1F)) {
            ItemStack mainTool;
            if (chance(random, 50))
                mainTool = new ItemStack(Items.IRON_SWORD);
            else {
                if (getY() <= 40) {
                    mainTool = new ItemStack(chance(random, 70) ? Items.IRON_PICKAXE : Items.IRON_SHOVEL);
                } else
                    mainTool = new ItemStack(chance(random, 50) ? Items.IRON_AXE : Items.IRON_SHOVEL);
            }
            equipStack(EquipmentSlot.MAINHAND, mainTool);
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
                    ordinal = 0
            ),
            cancellable = true
    )
    protected void changeReinforcementRequirements(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (spawnReinforcementCooldown > 0 || (!source.isOf(DamageTypes.MOB_ATTACK) &&
                !source.isOf(DamageTypes.MOB_PROJECTILE) &&
                !source.isOf(DamageTypes.MOB_ATTACK_NO_AGGRO) &&
                !source.isOf(DamageTypes.PLAYER_ATTACK) &&
                !source.isOf(DamageTypes.PLAYER_EXPLOSION) &&
                !source.isOf(DamageTypes.SONIC_BOOM) &&
                !source.isOf(DamageTypes.WITHER_SKULL) &&
                !source.isOf(DamageTypes.INDIRECT_MAGIC) &&
                !source.isOf(DamageTypes.STING) &&
                !source.isOf(DamageTypes.THORNS))
        ) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ZombieEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/EntityData;"
            )
    )
    protected void resetReinforcementCooldown(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        spawnReinforcementCooldown = REINFORCEMENT_COOLDOWN.getValue();
    }

    // interface methods

    @Override
    public void cringeMod$onEntityAddedToWorld(ServerWorld world, Vec3d pos) {
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
        if (this.getTarget() != null || !chance(random, SEARCH_TARGET_ON_SPAWN_CHANCE.getValue()) || !cringeMod$canAlertOthers()) return;
        var followRange = AttributeHelper.getValue(this, EntityAttributes.GENERIC_FOLLOW_RANGE, 35);
        var player = getWorld().getClosestPlayer(onSpawnTargetPredicate.setBaseMaxDistance(followRange * 0.7), this);
        if (player != null) {
            this.setTarget(player);
        }
    }

    @Unique
    protected void alertOthers() {
        if (getTarget() == null || isSilent()) return;
        var followRange = AttributeHelper.getValue(this, EntityAttributes.GENERIC_FOLLOW_RANGE, 35) * 0.7;
        var zombies = getWorld().getEntitiesByClass(net.minecraft.entity.mob.ZombieEntity.class, Box.of(getPos(), followRange, followRange / 1.5, followRange), mob -> mob.getTarget() == null && !(mob instanceof ZombifiedPiglinEntity) && !mob.isSilent());
        var chance = ALERT_OTHERS_CHANCE.getValue() + zombies.size() * 3;
        for (net.minecraft.entity.mob.ZombieEntity zombie : zombies)
            if (chance(random, chance))
                zombie.setTarget(getTarget());
    }

    @Unique
    protected boolean tryToAdaptToLight() {
        if (shouldAdaptToLight) {
            if (chance(random, LIGHT_ADAPTATION_CHANCE.getValue())) {
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
        this.alertOthersCooldown = range(random, ALERT_OTHERS_COOLDOWN.getValue());
    }

}