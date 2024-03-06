package me.xleiten.rebalance.api.game.event.world.entity;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public interface SpawnableEntity
{
    default void cringeMod$onEntityAddedToWorld(ServerWorld world, Vec3d pos) { }
}
