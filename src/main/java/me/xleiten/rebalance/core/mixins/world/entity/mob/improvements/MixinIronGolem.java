package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.math.RandomHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.util.AttributeHelper.*;

@Mixin(IronGolemEntity.class)
public abstract class MixinIronGolem extends MixinMobEntity
{
    protected MixinIronGolem(EntityType<? extends GolemEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason) {
        super.rebalanceMod$onFirstSpawn(world, random, reason);
        setBaseValue(this, EntityAttributes.GENERIC_MAX_HEALTH, MOB_IRON_GOLEM__MAX_HEALTH.value());
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", RandomHelper.range(random, MOB_IRON_GOLEM__MOVE_SPEED_BOOST.value())));
        setBaseValue(this, EntityAttributes.GENERIC_FOLLOW_RANGE, RandomHelper.range(random, MOB_IRON_GOLEM__FOLLOW_RANGE_BOOST.value()));
        setBaseValue(this, EntityAttributes.GENERIC_ATTACK_DAMAGE, base -> base + Math.sqrt(base));
        this.setHealth(getMaxHealth());
    }

    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    public void removeFireDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!source.isOf(DamageTypes.LAVA) && this.isOnFire()) {
            this.setFireTicks(0);
            cir.setReturnValue(false);
        }
    }
}
