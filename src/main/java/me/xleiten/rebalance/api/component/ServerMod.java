package me.xleiten.rebalance.api.component;

import me.xleiten.rebalance.api.component.events.ModEvents;
import me.xleiten.rebalance.api.component.events.ServerEvent;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public abstract class ServerMod extends Mod implements DedicatedServerModInitializer
{
    @Override
    public final void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> call(ModEvents.SERVER, new ServerEvent(server, ServerEvent.ServerState.STARTED)));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> call(ModEvents.SERVER, new ServerEvent(server, ServerEvent.ServerState.STOPPED)));
        ServerLifecycleEvents.SERVER_STARTING.register(server -> call(ModEvents.SERVER, new ServerEvent(server, ServerEvent.ServerState.STARTING)));
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> call(ModEvents.SERVER, new ServerEvent(server, ServerEvent.ServerState.STOPPING)));
        super.initialize();
    }
}
