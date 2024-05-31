package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.api.math.RandomHelper.chance;
import static me.xleiten.rebalance.api.math.RandomHelper.range;
import static me.xleiten.rebalance.util.AttributeHelper.*;

@Mixin(HuskEntity.class)
public abstract class MixinHuskEntity extends MixinZombieEntity
{
    protected MixinHuskEntity(EntityType<? extends ZombieEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void initCustomVars(EntityType<? extends ZombieEntity> entityType, World world, CallbackInfo ci) {
        this.cringeMod$setCanAdaptToLight(MOB_HUSK__SHOULD_ADAPT_TO_LIGHT.value());
        this.cringeMod$setBurnInDayLight(MOB_HUSK__SHOULD_BURN_IN_LIGHT.value());
        this.cringeMod$setCanAlertOthers(MOB_HUSK__SHOULD_ALERT_OTHERS.value());
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason) {
        super.rebalanceMod$onFirstSpawn(world, random, reason);

        addModifier(this, EntityAttributes.GENERIC_FOLLOW_RANGE, modifier("followRangeBoost", range(random, MOB_HUSK__FOLLOW_RANGE_MULT.value())));
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", range(random, (isBaby() ? MOB_HUSK__BABY_MOVE_SPEED_MULT : MOB_HUSK__MOVE_SPEED_MULT).value())));

        if (chance(random, MOB_HUSK__SPAWN_REINFORCEMENT_CHANCE.value()))
            setBaseValue(this, EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, range(random, MOB_HUSK__ZOMBIE_REINFORCEMENT.value()));

        if (chance(random, MOB_HUSK__BREAK_DOORS_CHANCE.value()))
            setCanBreakDoors(true);
    }

    @ModifyReturnValue(method = "burnsInDaylight", at = @At("RETURN"))
    public boolean burnsInDaylight(boolean original) {
        return cringeMod$canBurnInDayLight();
    }

    @Override
    protected void resetAlertCooldown() {
        this.alertOthersCooldown = range(random, MOB_HUSK__ALERT_OTHERS_COOLDOWN.value());
    }
}
