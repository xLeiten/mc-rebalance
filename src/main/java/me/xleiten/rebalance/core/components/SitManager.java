package me.xleiten.rebalance.core.components;

import me.xleiten.rebalance.Rebalance;
import me.xleiten.rebalance.api.component.Component;
import me.xleiten.rebalance.api.game.world.entity.ArmorStand;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SitManager extends Component<Rebalance>
{
    private static final ConcurrentHashMap<UUID, SittingPlayer> PLAYERS_SITTING = new ConcurrentHashMap<>();

    public SitManager(@NotNull Rebalance mod)
    {
        super("SitManager", mod);

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (hand == Hand.MAIN_HAND && hitResult != null) {
                if (player.getMainHandStack().isEmpty()) {
                    var pos = hitResult.getBlockPos();
                    if (isValidState(world.getBlockState(pos)) && isThereSpace(world, pos))
                        makePlayerSit(player, pos);
                }
            }
            return ActionResult.PASS;
        });

        ServerTickEvents.END_SERVER_TICK.register(world -> {
            PLAYERS_SITTING.forEach((id, data) -> {
                data.updateRotation();
                if (!isValidState(data.world.getBlockState(data.pos)) || !data.vehicle.hasPassengers())
                    makePlayerStandUp(data);
            });
        });

        ServerWorldEvents.UNLOAD.register((server, world) -> {
            PLAYERS_SITTING.forEach((id, data) -> data.vehicle.discard());
            PLAYERS_SITTING.clear();
        });
    }

    private static boolean isValidState(BlockState state) {
        return (state.isIn(BlockTags.SLABS) && state.get(SlabBlock.TYPE) == SlabType.BOTTOM) ||
                (state.isIn(BlockTags.STAIRS) && state.get(StairsBlock.HALF) == BlockHalf.BOTTOM);
    }

    private static boolean isThereSpace(World world, BlockPos clickedBlockPos) {
        return world.isAir(clickedBlockPos.up()) && world.isAir(clickedBlockPos.up(1));
    }

    private static void makePlayerSit(PlayerEntity player, BlockPos pos) {
        var playerId = player.getUuid();
        if (player.hasVehicle() || PLAYERS_SITTING.containsKey(playerId)) return;
        if (player.getPos().add(0, 0.75, 0).distanceTo(pos.toCenterPos()) > 1.75) return;

        var world = player.getWorld();
        var vehicle = new ArmorStandEntity(EntityType.ARMOR_STAND, world);
        vehicle.setPosition(pos.toCenterPos());
        vehicle.setInvisible(true);
        vehicle.setSilent(true);
        vehicle.setNoGravity(true);
        vehicle.setInvulnerable(true);
        ((ArmorStand) vehicle).cringeMod$setIsSmall(true);
        ((ArmorStand) vehicle).cringeMod$setIsMarker(true);
        vehicle.setHideBasePlate(true);
        var maxHealthInstance = vehicle.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (maxHealthInstance != null) maxHealthInstance.setBaseValue(1f);
        world.spawnEntity(vehicle);
        player.startRiding(vehicle, true);
        PLAYERS_SITTING.putIfAbsent(playerId, new SittingPlayer(player, vehicle, pos, world));
    }

    private static void makePlayerStandUp(SittingPlayer data) {
        data.vehicle.discard();
        PLAYERS_SITTING.remove(data.player.getUuid());
    }

    private record SittingPlayer(PlayerEntity player, ArmorStandEntity vehicle, BlockPos pos, World world)
    {
        public void updateRotation() {
            vehicle.setHeadYaw(player.getHeadYaw());
            vehicle.setPitch(player.getPitch());
            vehicle.setYaw(player.getYaw());
        }
    }
}
