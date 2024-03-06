package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs;

import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.util.math.DoubleRange;
import me.xleiten.rebalance.util.math.RandomHelper;
import me.xleiten.rebalance.util.math.Range;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.xleiten.rebalance.Settings.IRON_GOLEM_SETTINGS;
import static me.xleiten.rebalance.util.AttributeHelper.*;

@Mixin(IronGolemEntity.class)
public abstract class MixinIronGolem extends MixinMobEntity implements InitializableMobEntity
{
    @Unique private static final Option<Float> MAX_HEALTH = IRON_GOLEM_SETTINGS.option("max-health", 225f);
    @Unique private static final Option<DoubleRange> MOVE_SPEED_BOOST = IRON_GOLEM_SETTINGS.option("move-speed-boost", Range.create(0.1, 0.2));
    @Unique private static final Option<DoubleRange> FOLLOW_RANGE_BOOST = IRON_GOLEM_SETTINGS.option("follow-range", Range.create(24., 32));

    protected MixinIronGolem(EntityType<? extends GolemEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void cringeMod$onMobInitialize(ServerWorldAccess world, Random random, SpawnReason reason, Vec3d pos, Difficulty difficulty) {
        super.cringeMod$onMobInitialize(world, random, reason, pos, difficulty);
        setBaseValue(this, EntityAttributes.GENERIC_MAX_HEALTH, MAX_HEALTH.getValue());
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", RandomHelper.range(random, MOVE_SPEED_BOOST.getValue())));
        setBaseValue(this, EntityAttributes.GENERIC_FOLLOW_RANGE, RandomHelper.range(random, FOLLOW_RANGE_BOOST.getValue()));
        setBaseValue(this, EntityAttributes.GENERIC_ATTACK_DAMAGE, base -> base + Math.sqrt(base));
        this.setHealth(MAX_HEALTH.getValue());
    }

    @Inject(
            method = "damage",
            at = @At("TAIL")
    )
    public void removeFireIfNotLava(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!source.isOf(DamageTypes.LAVA) && this.isOnFire()) {
            this.setFireTicks(0);
        }
    }

    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    public void removeFireDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.isOf(DamageTypes.IN_FIRE)) {
            cir.setReturnValue(false);
        }
    }
}
