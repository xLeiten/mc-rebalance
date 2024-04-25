package me.xleiten.rebalance.core.components;

import me.xleiten.rebalance.Rebalance;
import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.component.Component;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.world.ServerLevel;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TabServerInfo extends Component<Rebalance>
{
    private final Option<Integer> REFRESH_TICKS = settings.option("refresh-ticks", 10);

    private int ticks = REFRESH_TICKS.getValue();

    public TabServerInfo(@NotNull Rebalance mod)
    {
        super("Server info in tab", mod);
        ServerTickEvents.END_SERVER_TICK.register(this::tick);
    }

    private void tick(MinecraftServer server) {
        if (ticks-- <= 0) {
            server.getPlayerManager().getPlayerList().forEach(player -> {
                player.networkHandler.sendPacket(new PlayerListHeaderS2CPacket(
                        buildHeader(player), buildFooter(player)
                ));
            });
            ticks = REFRESH_TICKS.getValue();
        }
    }

    public Text buildHeader(ServerPlayerEntity player) {
        return Text.literal("")
                .append(Text.literal("Время: ").append(Text.literal( LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) + "\n").formatted(Formatting.GOLD)))
                .append(Text.literal("Пинг: ").append(Text.literal( player.networkHandler.getLatency() + "\n").formatted(Formatting.GOLD)));
    }

    public Text buildFooter(ServerPlayerEntity player) {
        return Text.literal("\n")
                .append(Text.literal("Мобов: ").append(Text.literal(calcHostileMobs(player.getServerWorld()) + "\n").formatted(Formatting.GOLD)))
                .append(Text.literal("Направление: ").append(Text.literal(getLookDirection(player) + "\n").formatted(Formatting.GOLD)))
                .append(Text.literal("Перезапуск: ").append(Text.literal(mod.autoHardcoreWorldReset.getTotalRestarts() + "").formatted(Formatting.GOLD)))
                ;
    }

    public int calcHostileMobs(ServerWorld world) {
        int count = 0;
        for (Entity entity: ((ServerLevel) world).cringeMod$getEntityManager().getLookup().iterate()) {
            if (entity instanceof Monster) {
                count++;
            }
        }
        return count;
    }

    public String getLookDirection(ServerPlayerEntity player) {
        return switch (player.getMovementDirection()) {
            case NORTH -> "Север";
            case SOUTH -> "Юг";
            case WEST -> "Восток";
            case EAST -> "Запад";
            default -> "Дурка";
        };
    }
}
