package me.xleiten.rebalance.api.game.world.entity.mob;

public interface Creeper extends HostileMob
{

    void cringeMod$setCharged(boolean isCharged);

    boolean cringeMod$isCharged();

    void cringeMod$setExplosionRadius(int explosionRadius);
}
