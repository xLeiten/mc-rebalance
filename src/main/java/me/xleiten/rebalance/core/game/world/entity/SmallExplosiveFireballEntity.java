package me.xleiten.rebalance.core.game.world.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public final class SmallExplosiveFireballEntity extends SmallFireballEntity
{
    private final int power;

    public SmallExplosiveFireballEntity(World world, LivingEntity owner, double x, double y, double z, int power) {
        super(world, owner, 0, 0, 0);
        this.setPosition(x, y, z);
        this.power = power;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), power, this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING), World.ExplosionSourceType.MOB);
            this.discard();
        }
    }
}
