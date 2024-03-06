package me.xleiten.rebalance.api.game.event.world.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public final class EntityEvents
{
    public static final Event<Spawn> SPAWN = EventFactory.createArrayBacked(Spawn.class, listeners -> (entity, world, pos) -> {
        for (Spawn listener: listeners)
            listener.onSpawn(entity, world, pos);
    });

    @FunctionalInterface
    public interface Spawn
    {
        void onSpawn(Entity entity, ServerWorld world, Vec3d pos);
    }
}
