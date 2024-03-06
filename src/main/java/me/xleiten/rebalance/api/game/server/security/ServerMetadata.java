package me.xleiten.rebalance.api.game.server.security;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import me.xleiten.rebalance.api.config.types.ObjectType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public record ServerMetadata(String version, Text motd, int protocol, int maxPlayers, int onlinePlayers,
                             String serverIconPath) {

    public static class Type extends ObjectType<ServerMetadata> {

        @Override
        public @NotNull String getTypeKey() {
            return "SM";
        }

        @Override
        public @NotNull Class<ServerMetadata> getTypeClass() {
            return ServerMetadata.class;
        }

        @Override
        public @NotNull ServerMetadata deserialize(JsonObject element, JsonDeserializationContext context) {
            return new ServerMetadata(
                    element.get("version").getAsString(),
                    Text.of(element.get("motd").getAsString()),
                    element.get("protocol").getAsInt(),
                    element.get("max-players").getAsInt(),
                    element.get("online-players").getAsInt(),
                    element.get("server-icon").getAsString()
            );
        }

        @NotNull
        @Override
        public JsonObject serialize(ServerMetadata metadata, JsonSerializationContext context) {
            var object = new JsonObject();
            object.add("version", new JsonPrimitive(metadata.version));
            object.add("protocol", new JsonPrimitive(metadata.protocol));
            object.add("max-players", new JsonPrimitive(metadata.maxPlayers));
            object.add("online-players", new JsonPrimitive(metadata.onlinePlayers));
            object.add("motd", new JsonPrimitive(metadata.motd.getString()));
            object.add("server-icon", new JsonPrimitive(metadata.serverIconPath));
            return object;
        }
    }

}
