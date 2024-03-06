package me.xleiten.rebalance.core.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.xleiten.rebalance.Rebalance;
import me.xleiten.rebalance.api.component.Component;
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

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class SkinManager extends Component<Rebalance>
{
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path playersCacheDir = Path.of("players-skin-cache");

    public SkinManager(@NotNull Rebalance mod) {
        super("SkinManager", mod);

        createDirectory();

        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            var player = handler.getPlayer();
            if (player instanceof HumanEntity) return;

            getSkinInfo(player.getName().getString()).whenComplete((result, ex) -> {
                loadSkinFromFile(player.getGameProfile().getName()).ifPresentOrElse(
                        data -> ((SkinHolder) player).cringeMod$setSkinData(data, true),
                        () -> {
                            if (result != null)
                                ((SkinHolder) player).cringeMod$setSkinData(SkinData.copyFrom(result), true);
                        });
            });
        });

        ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> {
            var player = handler.getPlayer();
            if (player instanceof HumanEntity) return;
            saveSkinToFile(handler.getPlayer());
        }));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("skin").requires(ServerCommandSource::isExecutedByPlayer).then(argument("name", StringArgumentType.word()).executes(context -> {
                var playerName = StringArgumentType.getString(context, "name");
                getSkinInfo(playerName).whenComplete((result, ex) -> {
                    var player = (ServerPlayerEntity) context.getSource().getPlayer();
                    if (result != null) {
                        player.sendMessage(Text.of("Найден скин игрока '" + playerName + "'"));
                        ((SkinHolder) player).cringeMod$setSkinData(SkinData.copyFrom(result), true);
                    } else
                        player.sendMessage(Text.of("Игрока с таким ником не существует"));
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

    private Optional<SkinData> loadSkinFromFile(String playerName) {
        try {
            var path = playersCacheDir.resolve(playerName + ".json");
            if (Files.exists(path))
                return Optional.of(gson.fromJson(Files.newBufferedReader(path), SkinData.class));
        } catch (IOException ignored) {}
        return Optional.empty();
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

    public static CompletableFuture<Property> getSkinInfo(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
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


}
