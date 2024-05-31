package me.xleiten.rebalance.core.game.world.entity.mob.ai;

import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.math.RandomHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HostileEntityPlaceBlockGoal extends Goal
{
    private final HostileEntity mob;
    private final World world;

    private int blockPlaceCooldown = 20;

    public HostileEntityPlaceBlockGoal(@NotNull HostileEntity mob)
    {
        this.mob = mob;
        this.world = mob.getWorld();
    }

    @Override
    public void tick() {
        var target = mob.getTarget();
        if (target == null) return;

        var pos = mob.getBlockPos();
        if (!mob.isOnGround() && world.isAir(pos.down(2))) return;

        if (blockPlaceCooldown-- <= 0) {
            var state = world.getBlockState(pos.down());
            if (state.isReplaceable() || state.isAir()) {
                var stack = getAvailableBlockStack();
                if (stack != null) {
                    placeBlockWith(stack, pos.down());
                    mob.swingHand(Hand.MAIN_HAND);
                }
            }
            blockPlaceCooldown = RandomHelper.range(mob.getRandom(), Settings.AI_ZOMBIE_BLOCK_PLACE__PLACE_COOLDOWN.value());
        } else {
            if (blockPlaceCooldown == Settings.AI_ZOMBIE_BLOCK_PLACE__TICKS_AFTER_JUMP_TO_PLACE_BLOCK.value()) {
                mob.getJumpControl().setActive();
            }
        }
    }

    private void placeBlockWith(ItemStack stack, BlockPos pos) {
        var block = ((BlockItem) stack.getItem()).getBlock();
        var blockState = block.getDefaultState();
        world.setBlockState(pos, blockState, Block.NOTIFY_ALL);
        world.playSound(mob, pos, blockState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1, 1);
        world.emitGameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Emitter.of(mob, blockState));
        if (!Settings.AI_ZOMBIE_BLOCK_PLACE__INFINITE_BLOCKS.value())
            stack.decrement(1);
    }

    @Nullable
    private ItemStack getAvailableBlockStack() {
        for (ItemStack stack: mob.getHandItems()) {
            if (stack.getItem() instanceof BlockItem) return stack;
        }
        return null;
    }

    @Override
    public boolean canStart() {
        return hasValidTarget() && mob.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) && mob.getNavigation().isIdle() && getAvailableBlockStack() != null;
    }

    @Override
    public boolean canStop() {
        return !canStart();
    }

    @Override
    public boolean shouldContinue() {
        return canStart();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    private boolean hasValidTarget() {
        var target = mob.getTarget();
        return target != null && target.getY() > mob.getY();
    }
}
