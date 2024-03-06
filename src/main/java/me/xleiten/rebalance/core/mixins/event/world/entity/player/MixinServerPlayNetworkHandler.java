package me.xleiten.rebalance.core.mixins.event.world.entity.player;

import me.xleiten.rebalance.api.game.event.world.entity.player.ServerPlayerEvents;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class MixinServerPlayNetworkHandler
{
    @Shadow public ServerPlayerEntity player;

    @Inject(
            method = "onClientStatus",
            at = @At(
                    value = "TAIL"
            )
    )
    protected void onPlayerRespawn(ClientStatusC2SPacket packet, CallbackInfo ci) {
       if (packet.getMode() == ClientStatusC2SPacket.Mode.PERFORM_RESPAWN)
           ServerPlayerEvents.RESPAWN.invoker().onRespawn(player, player.getServerWorld(), player.getServer());
    }
}
