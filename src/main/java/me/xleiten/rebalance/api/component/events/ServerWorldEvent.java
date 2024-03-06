package me.xleiten.rebalance.api.component.events;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

public record ServerWorldEvent(@NotNull ServerWorld world, @NotNull MinecraftServer server, @NotNull WorldState state) implements ModEvent
{
    public enum WorldState
    {
        LOAD,
        UNLOAD
    }
}
