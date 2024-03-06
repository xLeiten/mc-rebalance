package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.world.entity.mob.Creeper;
import me.xleiten.rebalance.util.math.DoubleRange;
import me.xleiten.rebalance.util.math.IntRange;
import me.xleiten.rebalance.util.math.Range;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static me.xleiten.rebalance.util.AttributeHelper.*;
import static me.xleiten.rebalance.util.math.RandomHelper.*;
import static me.xleiten.rebalance.Settings.*;

@Mixin(net.minecraft.entity.mob.CreeperEntity.class)
public abstract class MixinCreeperEntity extends MixinHostileEntity implements Creeper
{
    @Unique private static final Option<IntRange> EXPLOSION_RADIUS = CREEPER_SETTINGS.option("explosion-radius", Range.create(3, 7));
    @Unique private static final Option<Double> CHARGED_CHANCE = CREEPER_SETTINGS.option("spawn-charged-chance", 3d);
    @Unique private static final Option<Boolean> EXPLODE_WITH_FIRE = CREEPER_SETTINGS.option("explode-with-fire", true);
    @Unique private static final Option<DoubleRange> MOVE_SPEED_MULT = CREEPER_SETTINGS.option("move-speed-mult", Range.create(0.2, 0.4));

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
        return instance.createExplosion(entity, x, y, z, explosionRadius * f, EXPLODE_WITH_FIRE.getValue(), explosionSourceType);
    }

    @Override
    public void cringeMod$onMobInitialize(ServerWorldAccess world, Random random, SpawnReason reason, Vec3d pos, Difficulty difficulty) {
        super.cringeMod$onMobInitialize(world, random, reason, pos, difficulty);
        if (chance(random, CHARGED_CHANCE.getValue())) cringeMod$setCharged(true);
        cringeMod$setExplosionRadius(range(random, EXPLOSION_RADIUS.getValue()));
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", range(random, MOVE_SPEED_MULT.getValue())));
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
