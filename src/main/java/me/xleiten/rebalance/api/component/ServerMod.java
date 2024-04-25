package me.xleiten.rebalance.api.component;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public abstract class ServerMod extends Mod implements DedicatedServerModInitializer
{
    @Override
    public final void onInitializeServer() {
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
        super.initialize();
    }

    protected abstract void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment environment);
}
