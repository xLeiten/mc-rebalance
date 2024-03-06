package me.xleiten.rebalance.api.game.world.npc;

import com.mojang.authlib.GameProfile;
import me.xleiten.rebalance.api.network.FakeNetworkHandler;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.message.ChatVisibility;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.scoreboard.Team;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;
import java.util.UUID;

public class HumanEntity extends ServerPlayerEntity
{
    private OnTickHandler onTickHandler;
    private OnClickHandler onClickHandler;

    private HumanEntity(ServerWorld world, GameProfile profile)
    {
        super(world.getServer(), world, profile, SyncedClientOptions.createDefault());
        this.networkHandler = new FakeNetworkHandler(world.getServer(), this, new ConnectedClientData(
                profile, 0,
                new SyncedClientOptions(
                        "en_us", 0, ChatVisibility.HIDDEN, false, 0, PlayerEntity.DEFAULT_MAIN_ARM, false, false
                )
        ));
    }

    public static HumanEntity create(@NotNull ServerWorld world, @NotNull String name, @NotNull UUID uuid) {
        return new HumanEntity(world, new GameProfile(uuid, name));
    }

    public static HumanEntity create(@NotNull ServerWorld world, @NotNull String name) {
        return new HumanEntity(world, new GameProfile(UUID.randomUUID(), name));
    }

    public static HumanEntity create(@NotNull ServerWorld world, @NotNull GameProfile profile) {
        return new HumanEntity(world, profile);
    }

    public HumanEntity position(@NotNull Vec3d pos) {
        setBoundingBox(Box.of(pos, 1, 1, 1));
        this.setPosition(pos);
        return this;
    }

    public HumanEntity position(@NotNull BlockPos pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
        return this;
    }

    public HumanEntity rotation(float yaw, float pitch) {
        this.setRotation(yaw, pitch);
        this.setHeadYaw(yaw);
        return this;
    }

    public HumanEntity pose(@NotNull EntityPose pose) {
        this.setPose(pose);
        return this;
    }

    public HumanEntity onInteract(@NotNull OnClickHandler handler) {
        this.onClickHandler = handler;
        return this;
    }

    public HumanEntity onTick(@NotNull OnTickHandler handler) {
        this.onTickHandler = handler;
        return this;
    }

    public HumanEntity initialize() {
        broadcastPackets(createSpawnPacket());
        getServerWorld().onPlayerConnected(this);
        return this;
    }

    public void tick() {
        if (onTickHandler != null)
            onTickHandler.onTick();
    }

    public void broadcastPackets(Packet<?>... packets) {
        for (Packet<?> packet: packets)
            getServerWorld().getServer().getPlayerManager().sendToAll(packet);
    }

    public void remove() {
        this.onDisconnect();
        server.getPlayerManager().remove(this);
        this.getTextStream().onDisconnect();
        networkHandler.disconnect(Text.of("bruh"));
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (onClickHandler != null)
            onClickHandler.onClick(player, hand);

        return ActionResult.PASS;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        broadcastPackets(new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, this));
        return super.createSpawnPacket();
    }


    // not working ServerPlayer stuff

    @Override
    public void setVelocity(Vec3d velocity) { }

    @Override
    public void setVelocity(double x, double y, double z) { }

    @Override
    public void setClientOptions(SyncedClientOptions settings) { }

    @Override
    public void increaseStat(Stat<?> stat, int amount) { }

    @Override
    public void resetStat(Stat<?> stat) { }

    @Nullable
    @Override public Team getScoreboardTeam() { return null; }

    @Override
    public void sleep(BlockPos pos) { }

    @Override
    public void openEditSignScreen(SignBlockEntity sign, boolean front) { }

    @Override
    public OptionalInt openHandledScreen(@Nullable NamedScreenHandlerFactory factory) { return OptionalInt.empty(); }

    @Override
    public void openHorseInventory(AbstractHorseEntity horse, Inventory inventory) { }
}
