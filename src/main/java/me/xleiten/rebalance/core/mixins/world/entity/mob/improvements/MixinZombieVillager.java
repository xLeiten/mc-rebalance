package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.api.math.RandomHelper.chance;
import static me.xleiten.rebalance.api.math.RandomHelper.range;
import static me.xleiten.rebalance.util.AttributeHelper.*;

@Debug(export = true)
@Mixin(ZombieVillagerEntity.class)
public abstract class MixinZombieVillager extends MixinZombieEntity
{
    protected MixinZombieVillager(EntityType<? extends ZombieVillagerEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void initCustomVars(EntityType<? extends ZombieEntity> entityType, World world, CallbackInfo ci) {
        this.cringeMod$setCanAdaptToLight(MOB_ZOMBIE_VILLAGER__SHOULD_ADAPT_TO_LIGHT.value());
        this.cringeMod$setBurnInDayLight(MOB_ZOMBIE_VILLAGER__SHOULD_ADAPT_TO_LIGHT.value());
        this.cringeMod$setCanAlertOthers(MOB_ZOMBIE_VILLAGER__SHOULD_ADAPT_TO_LIGHT.value());
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason) {
        super.rebalanceMod$onFirstSpawn(world, random, reason);

        addModifier(this, EntityAttributes.GENERIC_FOLLOW_RANGE, modifier("followRangeBoost", range(random, MOB_ZOMBIE_VILLAGER__FOLLOW_RANGE_MULT.value())));
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", range(random, (isBaby() ? MOB_ZOMBIE_VILLAGER__BABY_MOVE_SPEED_MULT : MOB_ZOMBIE_VILLAGER__MOVE_SPEED_MULT).value())));

        if (chance(random, MOB_ZOMBIE_VILLAGER__BREAK_DOORS_CHANCE.value()))
            setCanBreakDoors(true);

        if (chance(random, MOB_ZOMBIE_VILLAGER__SPAWN_ADAPTED_TO_LIGHT_CHANCE.value())) {
            shouldBurnInDayLight = false;
            shouldAdaptToLight = false;
        }

        setBaseValue(this, EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, 0);
    }

    @Override
    protected boolean burnsInDaylight(boolean original) {
        return cringeMod$canBurnInDayLight();
    }

    @Override
    protected void resetAlertCooldown() {
        this.alertOthersCooldown = range(random, MOB_ZOMBIE_VILLAGER__ALERT_OTHERS_COOLDOWN.value());
    }
}
