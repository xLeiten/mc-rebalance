package me.xleiten.rebalance.core.mixins.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer
{
    @ModifyArg(
            method = "startServer",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Thread;setPriority(I)V"
            ),
            index = 0
    )
    private static int onServerStart(int newPriority) {
        int available_processors = Runtime.getRuntime().availableProcessors();
        return switch (available_processors) {
            case 1, 2, 3, 4 -> 5;
            case 5, 6 -> 6;
            case 7, 8 -> 7;
            default -> 8;
        };
    }
}
