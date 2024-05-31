package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import me.xleiten.rebalance.api.game.world.entity.mob.Mob;
import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
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
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.xleiten.rebalance.Settings.AI__HOSTILE_FOLLOW_RANGE;
import static me.xleiten.rebalance.Settings.AI__LOOK_AT_TARGET_EVERY_TICK;
import static me.xleiten.rebalance.api.math.RandomHelper.range;
import static me.xleiten.rebalance.util.AttributeHelper.setBaseValue;

@Mixin(HostileEntity.class)
public abstract class MixinHostileEntity extends MixinMobEntity implements Mob
{
    @Shadow public abstract boolean shouldDropXp();

    protected MixinHostileEntity(EntityType<? extends MobEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason) {
        super.rebalanceMod$onFirstSpawn(world, random, reason);
        setBaseValue(this, EntityAttributes.GENERIC_ATTACK_DAMAGE, base -> base + Math.sqrt(base));
        setBaseValue(this, EntityAttributes.GENERIC_FOLLOW_RANGE,
                range(random, AI__HOSTILE_FOLLOW_RANGE.value()),
                instance -> instance.getBaseValue() < AI__HOSTILE_FOLLOW_RANGE.value().min
        );
    }

    @Inject(
            method = "tickMovement",
            at = @At("TAIL")
    )
    protected void constantLookAtTarget(CallbackInfo ci) {
        if (AI__LOOK_AT_TARGET_EVERY_TICK.value()) {
            var target = getTarget();
            if (target != null)
                getLookControl().lookAt(target);
        }
    }

    @Override
    public float rebalanceMod$calcBlockBreakingDelta(BlockPos pos) {
        var state = getWorld().getBlockState(pos);
        float f = state.getHardness(getWorld(), pos);
        if (f == -1.0F) {
            return 0.0F;
        } else {
            int i = !state.isToolRequired() || getMainHandStack().isSuitableFor(state) ? 30 : 100;
            return rebalanceMod$getBlockBreakingSpeed(state) / f / (float) i;
        }
    }

    @Override
    public float rebalanceMod$getBlockBreakingSpeed(BlockState state) {
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
