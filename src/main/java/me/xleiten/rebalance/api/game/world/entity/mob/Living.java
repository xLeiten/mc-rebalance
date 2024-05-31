package me.xleiten.rebalance.api.game.world.entity.mob;

import me.xleiten.rebalance.api.game.event.world.entity.SpawnableEntity;
import me.xleiten.rebalance.api.game.world.NbtHolder;
import me.xleiten.rebalance.api.game.world.entity.HealthDisplay;
import org.jetbrains.annotations.NotNull;

public interface Living extends SpawnableEntity, NbtHolder
{
    @NotNull HealthDisplay cringeMod$getHealthDisplay();

    default void regenHealth() {}

    default boolean cringeMod$canRegenHealth() {
        return true;
    }

    int cringeMod$getHealthRegenCooldown();

    void cringeMod$setHealthRegenCooldown(int cooldown);
}
