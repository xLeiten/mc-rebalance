package me.xleiten.rebalance.core.components;

import me.xleiten.rebalance.Rebalance;
import me.xleiten.rebalance.api.component.Component;
import me.xleiten.rebalance.api.component.events.ModEvents;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.event.world.entity.player.ServerPlayerEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static me.xleiten.rebalance.Settings.HARDCORE_WORLD_RESET;
import static net.minecraft.server.command.CommandManager.literal;

public final class AutoHardcoreWorldReset extends Component<Rebalance>
{
    private final Option<Integer> maxResetTime = HARDCORE_WORLD_RESET.option("reset-counter-ticks", 210);
    private final Option<Boolean> showSeconds = HARDCORE_WORLD_RESET.option("show-seconds", true);

    private final Option<Integer> totalRestarts = HARDCORE_WORLD_RESET.option("total-restarts", 0);

    private boolean isResetting = false;
    private int counter = maxResetTime.getValue();

    public AutoHardcoreWorldReset(@NotNull Rebalance mod) {
        super("auto-hardcore-world-reset", mod);

        on(ModEvents.MOD, event -> {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                dispatcher.register(literal("reset").requires(source -> source.hasPermissionLevel(4))
                        .then(literal("pause").executes(context -> {
                            context.getSource().getServer().getPlayerManager().broadcast(Text.of("Перезапуск мира поставлен на паузу"), false);
                            isResetting = false;
                            return 1;
                        }))
                        .then(literal("resume").executes(context -> {
                            context.getSource().getServer().getPlayerManager().broadcast(Text.of("Перезапуск мира запущен"), false);
                            isResetting = true;
                            return 1;
                        }))
                        .then(literal("cancel").executes(context -> {
                            context.getSource().getServer().getPlayerManager().broadcast(Text.of("Перезапуск мира отменен"), false);
                            isResetting = false;
                            counter = maxResetTime.getValue();
                            return 1;
                        }))
                );
            });

            ServerPlayerEvents.DEATH.register((player, world, server, cause) -> {
                if (isEnabled() && server.isHardcore() && isAllPlayersDead(server)) {
                    isResetting = true;
                }
                return ActionResult.PASS;
            });

            ServerTickEvents.END_SERVER_TICK.register(server -> {
                if (isResetting) {
                    if (counter > 0) {
                        if (showSeconds.getValue() && counter-- % 20 == 0)
                            server.getPlayerManager().broadcast(Text.of("Перезапуск мира через: " + (counter / 20) + " сек."), false);
                        return;
                    }
                    totalRestarts.setValue(totalRestarts.getValue() + 1);
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        deleteWorldFiles(server.getRunDirectory().toPath());
                        executeRestart();
                    }));
                    isResetting = false;
                    server.stop(false);
                }
            });
        });
    }

    public boolean isAllPlayersDead(MinecraftServer server) {
        return server.getPlayerManager().getPlayerList().stream().filter(ServerPlayerEntity::canTakeDamage).toList().isEmpty();
    }

    private void deleteWorldFiles(Path worldDir) {
        try (Stream<Path> files = Files.list(worldDir.resolve("world"))) {
            files.filter(file -> !file.endsWith("datapacks")).forEach(file -> {
                try {
                    if (Files.isDirectory(file))
                        FileUtils.deleteDirectory(file.toFile());
                    else
                        Files.delete(file);
                } catch (Exception exception) {
                    logger.warn("Error while deleting world files. " + exception.getMessage());
                }
            });
        } catch (Exception exception) {
            logger.warn("Error while deleting world files. " + exception.getMessage());
        }
    }

    private void executeRestart() {
        try {
            Runtime.getRuntime().exec("cmd /c start \"\" start.bat");
        } catch (IOException exception) {
            logger.warn("Restart error. " + exception.getMessage());
        }
    }

    public int getTotalRestarts() {
        return totalRestarts.getValue();
    }
}
