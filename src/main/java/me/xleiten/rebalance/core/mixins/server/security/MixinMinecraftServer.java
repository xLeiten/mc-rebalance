package me.xleiten.rebalance.core.mixins.server.security;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.server.MinecraftServer;
import net.minecraft.server.ServerMetadata;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.Optional;

import static me.xleiten.rebalance.Settings.SERVER_SETTINGS;

@Mixin(net.minecraft.server.MinecraftServer.class)
public abstract class MixinMinecraftServer implements MinecraftServer
{
    @Unique private static final Option<Boolean> HIDE_INFORMATION = SERVER_SETTINGS.option("hide-information", true);
    @Unique private static final Option<me.xleiten.rebalance.core.game.ServerMetadata> SERVER_METADATA = SERVER_SETTINGS.option("metadata", new me.xleiten.rebalance.core.game.ServerMetadata("bruh", Text.of("A Minecraft Server"), 0, 20, 0, "server-icon.png"));

    @Shadow
    public abstract boolean shouldEnforceSecureProfile();

    @Shadow @Nullable private ServerMetadata.@Nullable Favicon favicon;

    @ModifyReturnValue(
            method = "createMetadata",
            at = @At("RETURN")
    )
    public ServerMetadata onCreateMetadata(ServerMetadata original) {
        if (HIDE_INFORMATION.getValue()) {
            var metadata = SERVER_METADATA.getValue();
            return new ServerMetadata(
                    metadata.motd(),
                    Optional.of(new ServerMetadata.Players(metadata.maxPlayers(), metadata.onlinePlayers(), List.of())),
                    Optional.of(new ServerMetadata.Version(metadata.version(), metadata.protocol())),
                    Optional.ofNullable(favicon),
                    shouldEnforceSecureProfile()
            );
        }
        return original;
    }

    @ModifyArg(
            method = "loadFavicon",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;getFile(Ljava/lang/String;)Ljava/io/File;"
            )
    )
    public String getServerIconPath(String path) {
        return cringeMod$getServerIconPath();
    }

    @Override
    public String cringeMod$getServerIconPath() {
        return SERVER_METADATA.getValue().serverIconPath();
    }
}
