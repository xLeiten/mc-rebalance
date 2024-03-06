package me.xleiten.rebalance.core.components.hardcore_player_respawn;

import me.xleiten.rebalance.Rebalance;
import me.xleiten.rebalance.api.component.Component;
import me.xleiten.rebalance.api.game.event.world.entity.player.ServerPlayerEvents;
import me.xleiten.rebalance.core.components.hardcore_player_respawn.stages.SummoningStage;
import me.xleiten.rebalance.util.Messenger;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.server.command.CommandManager.literal;

public final class HardcorePlayerRespawn extends Component<Rebalance>
{
    private final ConcurrentHashMap<UUID, RespawnablePlayer> diedPlayers = new ConcurrentHashMap<>();

    public HardcorePlayerRespawn(@NotNull Rebalance mod) {
        super("HardcorePlayerRespawn", mod);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("revive-self").requires(source -> source.hasPermissionLevel(4) && source.isExecutedByPlayer()).executes(context -> {
                var diedSelf = diedPlayers.get(context.getSource().getPlayer().getUuid());
                if (diedSelf != null) {
                    Messenger.sendMessage("Ты воскрешен", context);
                    diedSelf.forceRevive();
                } else {
                    Messenger.sendMessage("Ты еще не умер.", context);
                }
                return 1;
            }));
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            diedPlayers.forEach((id, player) -> player.remove());
            diedPlayers.clear();
        });

        ServerPlayerEvents.DEATH.register((player, world, server, cause) -> {
            if (isEnabled() && server.isHardcore() && !player.isCreative() && !player.isSpectator()) {
                if (mod.autoHardcoreWorldReset.isEnabled() && mod.autoHardcoreWorldReset.isAllPlayersDead(server)) return ActionResult.PASS;
                var uuid = player.getUuid();
                var diedPlayer = diedPlayers.get(uuid);
                if (diedPlayer == null) {
                    var isInRitual = diedPlayers.entrySet().stream().anyMatch(entry -> entry.getValue().getRitualProcess().getCurrentStage() instanceof SummoningStage stage && stage.getParticipant().getUuid() != player.getUuid());
                    if (!isInRitual) {
                        player.changeGameMode(GameMode.SPECTATOR);
                        player.setHealth(player.getMaxHealth());
                        diedPlayers.put(uuid, new RespawnablePlayer(player));
                        return ActionResult.FAIL;
                    }
                }
            }
            return ActionResult.PASS;
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (isEnabled() && server.isHardcore()) {
                diedPlayers.entrySet().removeIf(entry -> {
                    var player = entry.getValue();
                    player.tick();
                    return player.isRevived();
                });
            }
        });
    }
}
