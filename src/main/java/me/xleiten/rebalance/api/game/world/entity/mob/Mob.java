package me.xleiten.rebalance.api.game.world.entity.mob;

import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public interface Mob extends InitializableMobEntity, Living
{
    float cringeMod$calcBlockBreakingDelta(BlockPos pos);

    float cringeMod$getBlockBreakingSpeed(BlockState state);
}
