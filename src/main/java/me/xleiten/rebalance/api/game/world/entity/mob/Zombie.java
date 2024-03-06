package me.xleiten.rebalance.api.game.world.entity.mob;

public interface Zombie extends HostileMob
{
    void cringeMod$setCanAlertOthers(boolean canAlert);

    boolean cringeMod$canAlertOthers();

    void cringeMod$setBurnInDayLight(boolean canBurn);

    boolean cringeMod$canBurnInDayLight();

    void cringeMod$setCanAdaptToLight(boolean canAdapt);

    boolean cringeMod$canAdaptToLight();
}
