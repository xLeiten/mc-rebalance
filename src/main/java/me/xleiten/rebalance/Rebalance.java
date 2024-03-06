package me.xleiten.rebalance;

import com.mojang.brigadier.arguments.BoolArgumentType;
import me.xleiten.rebalance.api.component.ServerMod;
import me.xleiten.rebalance.api.config.DynamicStorage;
import me.xleiten.rebalance.api.game.server.security.ServerMetadata;
import me.xleiten.rebalance.core.components.AutoHardcoreWorldReset;
import me.xleiten.rebalance.core.components.SitManager;
import me.xleiten.rebalance.core.components.SkinManager;
import me.xleiten.rebalance.core.components.TabServerInfo;
import me.xleiten.rebalance.core.components.hardcore_player_respawn.HardcorePlayerRespawn;
import me.xleiten.rebalance.core.components.hardcore_player_respawn.stages.AwaitingStage;
import me.xleiten.rebalance.util.math.DoubleRange;
import me.xleiten.rebalance.util.math.FloatRange;
import me.xleiten.rebalance.util.math.IntRange;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

import static me.xleiten.rebalance.util.Messenger.sendMessage;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class Rebalance extends ServerMod
{
    public static String MOD_ID = "rebalance";
    public static final Logger LOGGER = LoggerFactory.getLogger("Rebalance");

    public static final DynamicStorage CONFIG = DynamicStorage.create("rebalance-mod")
            .addTypeAdapter(new IntRange.Type())
            .addTypeAdapter(new DoubleRange.Type())
            .addTypeAdapter(new FloatRange.Type())
            .addTypeAdapter(new ServerMetadata.Type())
            .addTypeAdapter(new AwaitingStage.IngredientsInfo.Type())
            .setLogger(LOGGER)
            .load();

    public final AutoHardcoreWorldReset autoHardcoreWorldReset = new AutoHardcoreWorldReset(this);
    public final HardcorePlayerRespawn hardcorePlayerRespawn = new HardcorePlayerRespawn(this);
    public final SkinManager skinManager = new SkinManager(this);
    public final SitManager sitManager = new SitManager(this);
    public final TabServerInfo tabServerInfo = new TabServerInfo(this);

    @Override
    protected void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("storage").requires(source -> source.hasPermissionLevel(4))
                    .then(literal("save").then(argument("delete-unused", BoolArgumentType.bool()).executes(context -> {
                        CompletableFuture.runAsync(() -> CONFIG.save(BoolArgumentType.getBool(context, "delete-unused"), result -> sendMessage("Результат сохранения: " + result, context)));
                        return 1;
                    })))
                    .then(literal("load").executes(context -> {
                        CompletableFuture.runAsync(() -> CONFIG.load(result -> sendMessage("Результат загрузки: " + result, context)));
                        return 1;
                    }))
            );
        });
    }

    @Override
    public @NotNull DynamicStorage getStorage() {
        return CONFIG;
    }

    @Override
    public @NotNull Logger getLogger() {
        return LOGGER;
    }
}
