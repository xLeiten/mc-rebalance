package me.xleiten.rebalance.api.component;

import net.fabricmc.api.DedicatedServerModInitializer;

public abstract class ServerMod extends Mod implements DedicatedServerModInitializer
{
    @Override
    public final void onInitializeServer() {
        super.initialize();
    }
}
