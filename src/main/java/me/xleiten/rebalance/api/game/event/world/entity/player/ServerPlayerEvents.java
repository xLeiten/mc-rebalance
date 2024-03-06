package me.xleiten.rebalance.api.game.event.world.entity.player;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

public final class ServerPlayerEvents
{
    public static final Event<Death> DEATH = EventFactory.createArrayBacked(Death.class, listeners -> (player, world, server, source) -> {
        for (Death listener: listeners) {
            var result = listener.onDeath(player, world, server, source);
            if (result != ActionResult.PASS)
                return result;
        }
        return ActionResult.PASS;
    });

    public static final Event<Respawn> RESPAWN = EventFactory.createArrayBacked(Respawn.class, listeners -> (player, world, server) -> {
        for (Respawn listener: listeners)
            listener.onRespawn(player, world, server);
    });

    public static final Event<Damage> DAMAGE = EventFactory.createArrayBacked(Damage.class, listeners -> (player, source, damage) -> {
        for (Damage listener: listeners)
            listener.onDamage(player, source, damage);
    });

    @FunctionalInterface
    public interface Death
    {
        ActionResult onDeath(ServerPlayerEntity player, ServerWorld world, MinecraftServer server, DamageSource cause);
    }

    @FunctionalInterface
    public interface Respawn
    {
         void onRespawn(ServerPlayerEntity player, ServerWorld world, MinecraftServer server);
    }

    @FunctionalInterface
    public interface Damage
    {
        void onDamage(ServerPlayerEntity player, DamageSource cause, float damage);
    }
}
