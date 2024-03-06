package me.xleiten.rebalance.api.game.world;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerEntityManager;

public interface ServerLevel
{
    ServerEntityManager<Entity> cringeMod$getEntityManager();
}
