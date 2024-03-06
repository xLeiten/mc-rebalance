package me.xleiten.rebalance.core.components.hardcore_player_respawn.stages;

import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.world.entity.SkinHolder;
import me.xleiten.rebalance.api.game.world.npc.HumanEntity;
import me.xleiten.rebalance.api.game.world.staged_process.StageContext;
import net.minecraft.entity.EntityPose;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static me.xleiten.rebalance.Settings.HARDCORE_PLAYER_RESPAWN;

public final class RitualContext implements StageContext
{
    static final Option<Integer> RITUAL_TIME = HARDCORE_PLAYER_RESPAWN.option("ritual-time", 20 * 15);
    static final Option<Integer> RADIUS = HARDCORE_PLAYER_RESPAWN.option("ritual-safe-radius", 3);
    static final Option<Float> XP_NEEDED = HARDCORE_PLAYER_RESPAWN.option("xp-needed", 300f);
    static final int RADIUS_SQUARED = RADIUS.getValue() * RADIUS.getValue();

    public final UUID playerId;
    public final ServerPlayerEntity player;
    public final HumanEntity deadBody;
    public final Vec3d position;
    public final ServerWorld world;
    public final Random random;

    public RitualContext(@NotNull ServerPlayerEntity player)
    {
        this.player = player;
        this.playerId = player.getUuid();
        this.deadBody = createDeadBody(player);
        this.world = player.getServerWorld();
        this.position = player.getPos().add(0, 0.5, 0);
        this.random = world.getRandom();
    }

    private HumanEntity createDeadBody(ServerPlayerEntity of) {
        var human = HumanEntity.create(of.getServerWorld(), of.getName().getString())
                .position(of.getPos().subtract(0, 0.2, 0))
                .rotation(of.getYaw(), of.getPitch());

        var skinHolder = (SkinHolder) of;
        skinHolder.cringeMod$getSkinData().ifPresent(data -> ((SkinHolder) human).cringeMod$setSkinData(data, false));
        human.getDataTracker().set(((SkinHolder) human).cringeMod$getModelPartsTracker(), of.getDataTracker().get(skinHolder.cringeMod$getModelPartsTracker()));
        human.setInvulnerable(true);
        human.setGlowing(true);
        human.setPose(EntityPose.SWIMMING);
        return human.initialize();
    }

    @Nullable
    public ServerPlayerEntity getPlayer() {
        var player = world.getPlayerByUuid(playerId);
        return player == null ? null : (ServerPlayerEntity) player;
    }
}
