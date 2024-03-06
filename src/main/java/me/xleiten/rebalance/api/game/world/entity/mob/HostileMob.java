package me.xleiten.rebalance.api.game.world.entity.mob;

public interface HostileMob extends Mob
{
    default void setConstantLookAtTarget(boolean constantLook) { }
}
