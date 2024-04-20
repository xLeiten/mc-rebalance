package me.xleiten.rebalance.api.game.world.item.enchantment;

import org.jetbrains.annotations.NotNull;

public abstract class ServerEnchantment
{
    public final String name;

    public ServerEnchantment(@NotNull String name)
    {
        this.name = name;
    }
}
