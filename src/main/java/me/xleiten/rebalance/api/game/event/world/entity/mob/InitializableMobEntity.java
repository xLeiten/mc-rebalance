package me.xleiten.rebalance.api.game.event.world.entity.mob;

import me.xleiten.rebalance.api.game.event.world.entity.SpawnableEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;

public interface InitializableMobEntity extends SpawnableEntity
{
    default void cringeMod$onMobInitialize(ServerWorldAccess world, Random random, SpawnReason reason, Vec3d pos, Difficulty difficulty) { }
}
