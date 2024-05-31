package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.world.entity.mob.Spider;
import me.xleiten.rebalance.api.game.world.entity.mob.SpiderType;
import me.xleiten.rebalance.util.ParticleHelper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.api.math.RandomHelper.chance;
import static me.xleiten.rebalance.api.math.RandomHelper.range;
import static me.xleiten.rebalance.util.AttributeHelper.addModifier;
import static me.xleiten.rebalance.util.AttributeHelper.modifier;

@Mixin(net.minecraft.entity.mob.SpiderEntity.class)
public abstract class MixinSpiderEntity extends MixinHostileEntity implements Spider
{
    @Unique protected SpiderType spiderType = SpiderType.NORMAL;
    @Unique private int cobwebCooldown = range(random, MOB_SPIDER__COBWEB_COOLDOWN.value());

    protected MixinSpiderEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason) {
        super.rebalanceMod$onFirstSpawn(world, random, reason);
        spiderType = SpiderType.forBiome(world.getBiome(this.getBlockPos()).getKey().orElse(BiomeKeys.PLAINS));
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", range(random, MOB_SPIDER__MOVE_SPEED_BOOST.value())));
    }

    @NotNull
    @Override
    public SpiderType cringeMod$getVariant() {
        return spiderType;
    }

    @Override
    public void cringeMod$onCustomNbtDataWrite(NbtCompound nbt) {
        super.cringeMod$onCustomNbtDataWrite(nbt);
        nbt.putString("variant", spiderType.name);
    }

    @Override
    public void cringeMod$onCustomNbtDataRead(NbtCompound nbt) {
        super.cringeMod$onCustomNbtDataRead(nbt);
        spiderType = SpiderType.fromName(nbt.getString("variant"));
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void onTick(CallbackInfo ci) {
        if (isAlive() && cobwebCooldown-- <= 0) {
            cobwebCooldown = range(random, MOB_SPIDER__COBWEB_COOLDOWN.value());
            coverTargetInCobweb();
        }
    }

    @Override
    public boolean tryAttack(Entity entity) {
        if (super.tryAttack(entity)) {
            if (entity instanceof LivingEntity target) {
                int effectSeconds = switch (this.getWorld().getDifficulty()) {
                    case NORMAL -> 7;
                    case HARD -> 15;
                    default -> 0;
                };
                if (effectSeconds > 0) {
                    switch (spiderType) {
                        case JUNGLE -> {
                            target.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, effectSeconds * 20));
                            target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, effectSeconds * 20));
                        }
                        case DARK_FOREST -> {
                            target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, effectSeconds * 20));
                            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, effectSeconds * 20));
                        }
                        case DESERT -> {
                            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, effectSeconds * 20));
                            target.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, effectSeconds * 20));
                        }
                        case CAVE -> {
                            target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, effectSeconds * 20, 0));
                            target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, effectSeconds * 20, 0));
                            target.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, effectSeconds * 20, 0));
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Unique
    protected void coverTargetInCobweb() {
        var target = getTarget();
        if (target != null && chance(random, MOB_SPIDER__COBWEB_CHANCE.value()) && MOB_SPIDER__COBWEB_DISTANCE.value().isIn((double) this.distanceTo(target)) && this.canSee(target)) {
            var world = getWorld();
            var pos = target.getBlockPos();
            var state = world.getBlockState(pos);
            if (state.isReplaceable()) {
                world.setBlockState(pos, Blocks.COBWEB.getDefaultState());
                world.playSound(((SpiderEntity) (Object) this), pos, SoundEvents.ENTITY_LLAMA_SPIT, SoundCategory.HOSTILE, 1, 1);
                ParticleHelper.spawnInLine((ServerWorld) world, ParticleTypes.SPIT, getEyePos(), pos.toCenterPos(), 10);
            }
        }
    }
}
