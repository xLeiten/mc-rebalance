package me.xleiten.rebalance;

import com.mojang.brigadier.CommandDispatcher;
import me.xleiten.rebalance.api.component.ServerMod;
import me.xleiten.rebalance.api.config.DynamicStorage;
import me.xleiten.rebalance.core.components.AutoHardcoreWorldReset;
import me.xleiten.rebalance.core.components.OfflineSkins;
import me.xleiten.rebalance.core.components.PlayerSitting;
import me.xleiten.rebalance.core.components.TabServerInfo;
import me.xleiten.rebalance.core.components.hardcore_player_respawn.HardcorePlayerRespawn;
import me.xleiten.rebalance.core.components.hardcore_player_respawn.IngredientsInfo;
import me.xleiten.rebalance.core.game.world.item.ArmorConfig;
import me.xleiten.rebalance.util.StringUtils;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.literal;

public final class Rebalance extends ServerMod
{
    public static String MOD_ID = "rebalance";

    public static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.capitalize(MOD_ID));
    public static final DynamicStorage MAIN_STORAGE = DynamicStorage.create(MOD_ID)
            .addTypeAdapters(
                    new IngredientsInfo.Type(),
                    new ArmorConfig.Type()
            )
            .clearUnusedOnSave(true)
            .setLogger(LOGGER)
            .load();

    public final AutoHardcoreWorldReset autoHardcoreWorldReset = new AutoHardcoreWorldReset(this);
    public final HardcorePlayerRespawn hardcorePlayerRespawn = new HardcorePlayerRespawn(this);
    public final OfflineSkins skinManager = new OfflineSkins(this);
    public final PlayerSitting sitManager = new PlayerSitting(this);
    public final TabServerInfo tabServerInfo = new TabServerInfo(this);

    @Override
    protected void onInitialize() {

    }

    @Override
    protected void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("rm-config").requires(source -> source.hasPermissionLevel(4))
                .then(literal("load").executes(context -> {
                    CompletableFuture.runAsync(() -> storage.load(result -> context.getSource().sendFeedback(() -> Text.of("Результат операции: " + result), true)));
                    return 1;
                }))
                .then(literal("save").executes(context -> {
                    CompletableFuture.runAsync(() -> storage.save(result -> context.getSource().sendFeedback(() -> Text.of("Результат операции: " + result), true)));
                    return 1;
                }))
        );
    }

    @Override
    public @NotNull DynamicStorage getStorage() {
        return MAIN_STORAGE;
    }

    @Override
    public @NotNull Logger getLogger() {
        return LOGGER;
    }
}
