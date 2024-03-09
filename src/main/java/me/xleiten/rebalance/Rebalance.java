package me.xleiten.rebalance;

import com.mojang.brigadier.CommandDispatcher;
import me.xleiten.rebalance.api.component.ServerMod;
import me.xleiten.rebalance.api.config.DynamicStorage;
import me.xleiten.rebalance.core.components.AutoHardcoreWorldReset;
import me.xleiten.rebalance.core.components.SitManager;
import me.xleiten.rebalance.core.components.SkinManager;
import me.xleiten.rebalance.core.components.TabServerInfo;
import me.xleiten.rebalance.core.components.hardcore_player_respawn.HardcorePlayerRespawn;
import me.xleiten.rebalance.core.components.hardcore_player_respawn.stages.AwaitingStage;
import me.xleiten.rebalance.core.game.ServerMetadata;
import me.xleiten.rebalance.util.StringUtils;
import me.xleiten.rebalance.util.math.DoubleRange;
import me.xleiten.rebalance.util.math.FloatRange;
import me.xleiten.rebalance.util.math.IntRange;
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
    //public static final StorageManager STORAGE_MANAGER = new StorageManager();
    public static final DynamicStorage MAIN_STORAGE = DynamicStorage.create(MOD_ID)
            //.addTo(STORAGE_MANAGER)
            .addTypeAdapters(
                    new IntRange.Type(),
                    new DoubleRange.Type(),
                    new FloatRange.Type(),
                    new ServerMetadata.Type(),
                    new AwaitingStage.IngredientsInfo.Type()
            )
            .setLogger(LOGGER)
            .load();

    public final AutoHardcoreWorldReset autoHardcoreWorldReset = new AutoHardcoreWorldReset(this);
    public final HardcorePlayerRespawn hardcorePlayerRespawn = new HardcorePlayerRespawn(this);
    public final SkinManager skinManager = new SkinManager(this);
    public final SitManager sitManager = new SitManager(this);
    public final TabServerInfo tabServerInfo = new TabServerInfo(this);

    @Override
    protected void onInitialize() {

    }

    @Override
    protected void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("storage").requires(source -> source.hasPermissionLevel(4))
                .then(literal("load").executes(context -> {
                    CompletableFuture.runAsync(() -> storage.load(result -> context.getSource().sendFeedback(() -> Text.of("Результат операции: " + result), true)));
                    return 1;
                }))
                .then(literal("save").executes(context -> {
                    CompletableFuture.runAsync(() -> storage.save(result -> context.getSource().sendFeedback(() -> Text.of("Результат операции: " + result), true)));
                    return 1;
                }))
        );

       /* dispatcher.register(literal("storage").then(argument("name", StringArgumentType.word())
                .then(literal("save")
                        .executes(context -> {
                            STORAGE_MANAGER.getStorage(StringArgumentType.getString(context, "name")).ifPresent(storage1 -> {
                                CompletableFuture.runAsync(() -> {
                                    storage1.save(result -> context.getSource().sendFeedback(() -> Text.of("Результат операции: " + result), true))
                                });
                            });
                            return 1;
                        })
                ))
        ));*/
    }

    @Override
    public @NotNull DynamicStorage getStorage() {
        return MAIN_STORAGE;
    }

    /*@Override
    public @NotNull StorageManager getStorageManager() {
        return STORAGE_MANAGER;
    }*/

    @Override
    public @NotNull Logger getLogger() {
        return LOGGER;
    }
}
