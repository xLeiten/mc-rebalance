package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.game.world.entity.mob.Creeper;
import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.api.math.RandomHelper.chance;
import static me.xleiten.rebalance.api.math.RandomHelper.range;
import static me.xleiten.rebalance.util.AttributeHelper.addModifier;
import static me.xleiten.rebalance.util.AttributeHelper.modifier;

@Mixin(net.minecraft.entity.mob.CreeperEntity.class)
public abstract class MixinCreeperEntity extends MixinHostileEntity implements Creeper
{
    @Shadow private int explosionRadius;
    @Shadow @Final private static TrackedData<Boolean> CHARGED;

    protected MixinCreeperEntity(EntityType<? extends HostileEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Redirect(
            method = "explode",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;")
    )
    public Explosion createExplosion(World instance, Entity entity, double x, double y, double z, float power, World.ExplosionSourceType explosionSourceType, @Local float f) {
        return instance.createExplosion(entity, x, y, z, explosionRadius * f, MOB_CREEPER__EXPLODE_WITH_FIRE.value(), explosionSourceType);
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason) {
        super.rebalanceMod$onFirstSpawn(world, random, reason);
        if (chance(random, Settings.MOB_CREEPER__CHARGED_CHANCE.value())) cringeMod$setCharged(true);
        cringeMod$setExplosionRadius(range(random, MOB_CREEPER__EXPLOSION_RADIUS.value()));
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", range(random, MOB_CREEPER__MOVE_SPEED_MULT.value())));
    }

    @Override
    public void cringeMod$setCharged(boolean isCharged) {
        getDataTracker().set(CHARGED, isCharged);
    }

    @Override
    public boolean cringeMod$isCharged() {
        return getDataTracker().get(CHARGED);
    }

    @Override
    public void cringeMod$setExplosionRadius(int explosionRadius) {
        this.explosionRadius = Math.max(explosionRadius, 0);
    }
}
