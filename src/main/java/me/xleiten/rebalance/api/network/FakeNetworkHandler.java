package me.xleiten.rebalance.api.network;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;

public final class FakeNetworkHandler extends ServerPlayNetworkHandler
{
    public final static ClientConnection FAKE_CONNECTION = new FakeConnection();

    public FakeNetworkHandler(MinecraftServer server, ServerPlayerEntity player,  ConnectedClientData data) {
        super(server, FAKE_CONNECTION, player, data);
    }

    @Override
    public void send(Packet<?> packet, @Nullable PacketCallbacks callbacks) { }

    public static class FakeConnection extends ClientConnection
    {
        private FakeConnection() {
            super(NetworkSide.CLIENTBOUND);
        }

        @Override
        public void setInitialPacketListener(PacketListener packetListener) {

        }

        @Override
        public void setupEncryption(Cipher decryptionCipher, Cipher encryptionCipher) {

        }

        @Override
        public void setPacketListener(PacketListener packetListener) { }

    }
}
