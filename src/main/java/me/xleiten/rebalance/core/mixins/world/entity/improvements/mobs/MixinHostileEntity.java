package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs;

import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.world.entity.mob.Mob;
import me.xleiten.rebalance.util.math.DoubleRange;
import me.xleiten.rebalance.util.math.Range;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.util.AttributeHelper.*;
import static me.xleiten.rebalance.util.math.RandomHelper.range;

@Mixin(HostileEntity.class)
public abstract class MixinHostileEntity extends MixinMobEntity implements Mob
{
    @Unique private static final Option<DoubleRange> HOSTILE_FOLLOW_RANGE = AI_SETTINGS.option("hostile-follow-range", Range.create(32.0, 52.0));
    @Unique private static final Option<Boolean> LOOK_AT_TARGET_EVERY_TICK = AI_SETTINGS.option("look-at-target-every-tick", true);

    @Shadow
    public abstract boolean shouldDropXp();

    protected MixinHostileEntity(EntityType<? extends MobEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void cringeMod$onMobInitialize(ServerWorldAccess world, Random random, SpawnReason reason, Vec3d pos, Difficulty difficulty) {
        super.cringeMod$onMobInitialize(world, random, reason, pos, difficulty);
        setBaseValue(this, EntityAttributes.GENERIC_ATTACK_DAMAGE, base -> base + Math.sqrt(base));
        setBaseValue(this, EntityAttributes.GENERIC_FOLLOW_RANGE,
                range(random, HOSTILE_FOLLOW_RANGE.getValue()),
                instance -> instance.getBaseValue() < HOSTILE_FOLLOW_RANGE.getValue().min
        );
    }

    @Inject(
            method = "tickMovement",
            at = @At("TAIL")
    )
    protected void constantLookAtTarget(CallbackInfo ci) {
        if (LOOK_AT_TARGET_EVERY_TICK.getValue()) {
            var target = getTarget();
            if (target != null)
                getLookControl().lookAt(target);
        }
    }

    @Override
    public float cringeMod$calcBlockBreakingDelta(BlockPos pos) {
        var state = getWorld().getBlockState(pos);
        float f = state.getHardness(getWorld(), pos);
        if (f == -1.0F) {
            return 0.0F;
        } else {
            int i = !state.isToolRequired() || getMainHandStack().isSuitableFor(state) ? 30 : 100;
            return cringeMod$getBlockBreakingSpeed(state) / f / (float) i;
        }
    }

    @Override
    public float cringeMod$getBlockBreakingSpeed(BlockState state) {
        float f = getMainHandStack().getMiningSpeedMultiplier(state);
        if (f > 1.0F) {
            int i = EnchantmentHelper.getEfficiency(this);
            ItemStack itemStack = this.getMainHandStack();
            if (i > 0 && !itemStack.isEmpty()) {
                f += (float) (i * i + 1);
            }
        }
        if (StatusEffectUtil.hasHaste(this)) {
            f *= 1.0F + (float) (StatusEffectUtil.getHasteAmplifier(this) + 1) * 0.2F;
        }
        var miningFatigueEffect = getStatusEffect(StatusEffects.MINING_FATIGUE);
        if (miningFatigueEffect != null) {
            float g = switch (miningFatigueEffect.getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };
            f *= g;
        }
        if (this.isSubmergedIn(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) {
            f /= 5.0F;
        }
        if (!this.isOnGround()) {
            f /= 5.0F;
        }
        return f;
    }
}
