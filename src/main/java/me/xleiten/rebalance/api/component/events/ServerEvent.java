package me.xleiten.rebalance.api.component.events;

import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public record ServerEvent(@NotNull MinecraftServer server, @NotNull ServerState stage) implements ModEvent
{
    public enum ServerState
    {
        STARTING,
        STARTED,
        STOPPING,
        STOPPED
    }
}
