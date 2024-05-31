package me.xleiten.rebalance.core.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.xleiten.rebalance.Rebalance;
import me.xleiten.rebalance.api.component.Component;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.world.entity.SkinData;
import me.xleiten.rebalance.api.game.world.entity.SkinHolder;
import me.xleiten.rebalance.api.game.world.npc.HumanEntity;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class OfflineSkins extends Component<Rebalance>
{
    private final Option<String> cacheFolderPath = settings.option("cache-folder-path", "players-skin-cache");
    private final Gson gson = new Gson();
    private final Path playersCacheDir = Path.of(cacheFolderPath.value());

    public OfflineSkins(@NotNull Rebalance mod) {
        super("Offline skins", mod);
        createDirectory();

        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            if (!isEnabled()) return;
            var player = handler.getPlayer();
            if (player instanceof HumanEntity) return;
            getSkinForPlayer(player.getGameProfile().getName(), data -> data.ifPresent(
                    skin -> ((SkinHolder) player).cringeMod$setSkinData(skin, true)
            ));
        });

        ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> {
            if (!isEnabled()) return;
            var player = handler.getPlayer();
            if (player instanceof HumanEntity) return;
            saveSkinToFile(handler.getPlayer());
        }));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("skin").requires(ServerCommandSource::isExecutedByPlayer).then(argument("name", StringArgumentType.word()).executes(context -> {
                var player = (ServerPlayerEntity) context.getSource().getPlayer();
                if (!isEnabled()) {
                    player.sendMessage(Text.of("Установка скинов отключена"));
                    return 1;
                }
                getSkinForPlayer(player.getGameProfile().getName(), (data) -> {
                    data.ifPresentOrElse((skin) -> {
                        player.sendMessage(Text.of("Найден скин игрока '" + name + "'"));
                        ((SkinHolder) player).cringeMod$setSkinData(skin, true);
                    }, () -> {
                        player.sendMessage(Text.of("Игрока с таким ником не существует"));
                    });
                });
                return 1;
            })));
        });
    }

    private void createDirectory() {
        try {
            if (!Files.exists(playersCacheDir)) Files.createDirectories(playersCacheDir);
        } catch (IOException exception) {
            logger.error("Cannot create directory for players skin cache.", exception);
        }
    }

    private CompletableFuture<Optional<SkinData>> loadSkinFromFile(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var path = playersCacheDir.resolve(playerName + ".json");
                if (Files.exists(path))
                    return Optional.of(gson.fromJson(Files.newBufferedReader(path), SkinData.class));
            } catch (Exception ignored) { }
            return Optional.empty();
        });
    }

    private void saveSkinToFile(ServerPlayerEntity player) {
        ((SkinHolder) player).cringeMod$getSkinData().ifPresent(data -> {
            try {
                var path = playersCacheDir.resolve(player.getName().getString() + ".json");
                try (var writer = Files.newBufferedWriter(path)) {
                    writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(data));
                    writer.flush();
                }
            } catch (IOException ignored) {
            }
        });
    }

    public static CompletableFuture<Property> getSkinInfo(@NotNull String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            if (playerName.isEmpty()) throw new IllegalArgumentException("Player name cannot be empty");
            try (var uuidService = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName).openStream()))) {
                var uuid = JsonParser.parseReader(uuidService).getAsJsonObject().get("id").toString().replaceAll("\"", "");
                try (var profileService = new BufferedReader(new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false").openStream()))) {
                    var texture = JsonParser.parseReader(profileService).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                    return new Property("textures", texture.get("value").getAsString(), texture.get("signature").getAsString());
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    public void getSkinForPlayer(String playerName, Consumer<Optional<SkinData>> data) {
        loadSkinFromFile(name).whenComplete((fileResult, ex1) -> {
            if (fileResult != null && fileResult.isPresent()) {
                data.accept(fileResult);
            } else {
                getSkinInfo(name).whenComplete((result, ex2) -> {
                    if (result != null) {
                        data.accept(Optional.of(SkinData.copyFrom(result)));
                    } else {
                        data.accept(Optional.empty());
                    }
                });
            }
        });
    }

}
