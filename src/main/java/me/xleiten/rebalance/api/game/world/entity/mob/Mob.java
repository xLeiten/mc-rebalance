package me.xleiten.rebalance.api.game.world.entity.mob;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;

public interface Mob extends Living
{
    float rebalanceMod$calcBlockBreakingDelta(BlockPos pos);

    float rebalanceMod$getBlockBreakingSpeed(BlockState state);

    void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason);
}
