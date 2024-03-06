package me.xleiten.rebalance.api.component.events;

import me.xleiten.rebalance.api.component.Mod;
import org.jetbrains.annotations.NotNull;

public record ModInitializeEvent(@NotNull Mod mod) implements ModEvent
{

}
