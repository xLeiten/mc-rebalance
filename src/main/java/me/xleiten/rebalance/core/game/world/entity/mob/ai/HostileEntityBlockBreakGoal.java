package me.xleiten.rebalance.core.game.world.entity.mob.ai;

import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.game.world.entity.mob.Mob;
import me.xleiten.rebalance.api.math.RandomHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HostileEntityBlockBreakGoal extends Goal
{
    private final MobEntity mob;
    private final EntityNavigation navigation;
    private final World world;
    private final Random random;

    private BlockPos lastNavigationPos;
    private BlockPos miningPos;

    private float checkBlocksCooldown;
    private float breakTicks = 0;

    public HostileEntityBlockBreakGoal(@NotNull HostileEntity mob)
    {
        this.mob = mob;
        this.world = mob.getWorld();
        this.random = mob.getRandom();
        this.navigation = mob.getNavigation();
        this.checkBlocksCooldown = RandomHelper.range(random, Settings.AI_ZOMBIE_BLOCK_BREAK__SEARCH_COOLDOWN.value());
    }

    @Override
    public boolean canStart() {
        return !mob.isAiDisabled() && shouldStartBreakingBlocks();
    }

    public boolean shouldStartBreakingBlocks() {
        if (mob.getTarget() == null ||
                !(mob.getStackInHand(Hand.MAIN_HAND).getItem() instanceof ToolItem) ||
                !mob.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)
        ) {
            return false;
        }

        return checkTargetPos();
    }

    private boolean checkTargetPos() {
        var path = navigation.getCurrentPath();
        if (path != null) {
            var lastNode = path.getEnd();
            if (lastNode != null) {
                var blockPos = lastNode.getBlockPos();
                if (mob.squaredDistanceTo(blockPos.toCenterPos()) <= 3) {
                    lastNavigationPos = blockPos;
                    return true;
                } else
                    return false;
            }
        }

        // will use self position if path is empty
        lastNavigationPos = mob.getBlockPos();
        return true;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public boolean canStop() {
        return !shouldStartBreakingBlocks();
    }

    @Override
    public void tick() {
        var target = mob.getTarget();
        if (target == null) return;

        if (checkBlocksCooldown-- <= 0) {
            checkBlocksCooldown = RandomHelper.range(random, Settings.AI_ZOMBIE_BLOCK_BREAK__SEARCH_COOLDOWN.value());
            var pos = findNextPosTo(target);
            if (pos != null) {
                if (miningPos == null) {
                    if (canBreakBlock(world.getBlockState(pos)))
                        miningPos = pos;
                }
            }
        }

        if (miningPos != null) {
            if (breakTicks < Settings.AI_ZOMBIE_BLOCK_BREAK__MAX_BREAK_TICKS.value()) {
                if (mob.getPos().distanceTo(miningPos.toCenterPos()) <= 3) {
                    var stage = (int) (((Mob) mob).rebalanceMod$calcBlockBreakingDelta(miningPos) * breakTicks * 10);
                    if (stage < 10) {
                        setBreakProgress(miningPos, stage);
                        mob.getLookControl().lookAt(miningPos.toCenterPos());
                        mob.swingHand(Hand.MAIN_HAND);
                    } else {
                        mob.getWorld().breakBlock(miningPos, true, mob);
                        checkBlocksCooldown = RandomHelper.range(random, Settings.AI_ZOMBIE_BLOCK_BREAK__SEARCH_COOLDOWN.value());
                        stop();
                        return;
                    }
                    breakTicks++;
                } else {
                    stop();
                }
            } else {
                stop();
            }
        }

        if (!mob.isAlive())
            stop();
    }

    @Nullable
    private BlockPos findNextPosTo(LivingEntity target) {
        var pos = lastNavigationPos;
        if (pos == null) return null;

        if (isValidPos(pos)) return pos;

        var mineUp = false;

        if (mob.getBlockY() < target.getBlockY()) {
            var upPos = pos.up(2);
            if (isValidPos(upPos)) {
                return upPos;
            } else {
                mineUp = true;
            }
        } else if (mob.getBlockY() > target.getBlockY()) {
            var deltaY = Math.abs(mob.getBlockY() - target.getBlockY());
            if (Math.abs(target.getBlockX() - mob.getBlockX()) < deltaY && Math.abs(target.getBlockZ() - mob.getBlockZ()) < deltaY) {
                var steppingPos = mob.getSteppingPos();
                if (isValidPos(steppingPos))
                    return steppingPos;
            }
        }

        var targetPos = target.getBlockPos();
        var deltaX = Integer.compare(targetPos.getX(), pos.getX());
        var deltaZ = Integer.compare(targetPos.getZ(), pos.getZ());
        var nextPos = pos.add(deltaX, 1, deltaZ);

        if (deltaX != 0 && deltaZ != 0) {
            var sidePosX = pos.add(deltaX, 1, 0);
            var sidePosZ = pos.add(0, 1, deltaZ);

            var sidePos = Math.abs(targetPos.getX() - pos.getX()) >= Math.abs(targetPos.getZ() - pos.getZ()) ? checkOrSwap(sidePosX, sidePosZ, mineUp) : checkOrSwap(sidePosZ, sidePosX, mineUp);
            if (sidePos != null) {
                nextPos = sidePos;
            }
        }

        var result = getNextRelative(nextPos, mineUp);
        if (result != null) {
            return result;
        }

        nextPos = pos.up();
        if (mineUp) {
            if (deltaX == 0 && deltaZ == 0) {
                for (BlockPos sided: getSidedOnY(nextPos)) {
                    if (shouldMine(sided, true)) {
                        return getNextRelative(sided, true);
                    }
                }
            } else {
                var east = nextPos.east();
                var west = nextPos.west();
                var south = nextPos.south();
                var north = nextPos.north();

                if (deltaZ == 0) {
                    if (shouldMine(north, true)) {
                        return getNextRelative(north, true);
                    }
                    if (shouldMine(south, true)) {
                        return getNextRelative(south, true);
                    }

                    var next = deltaX == 1 ? east : west;
                    if (shouldMine(next, true)) {
                        return getNextRelative(next, true);
                    }
                } else if (deltaX == 0) {
                    if (shouldMine(west, true)) {
                        return getNextRelative(west, true);
                    }
                    if (shouldMine(east, true)) {
                        return getNextRelative(east, true);
                    }
                    var next = deltaZ == 1 ? north : south;
                    if (shouldMine(next, true))
                        return getNextRelative(next, true);
                }
            }
        }

        return null;
    }

    private BlockPos[] getSidedOnY(BlockPos center) {
        return new BlockPos[] { center.east(), center.west(), center.north(), center.south() };
    }

    private boolean shouldMine(BlockPos start, boolean mineUp) {
        if (mineUp) {
            return navigation.isValidPosition(start) || isValidPos(start) || isValidPos(start.up());
        } else {
            var downPos = start.down();
            return isValidPos(start) || isValidPos(downPos) || navigation.isValidPosition(downPos);
        }
    }

    @Nullable
    private BlockPos getNextRelative(BlockPos start, boolean mineUp) {
        return nonAirOrNull(start) != null ? start : (mineUp ? nonAirOrNull(start.up()) : nonAirOrNull(start.down()));
    }

    @Nullable
    private BlockPos checkOrSwap(BlockPos pos1, BlockPos pos2, boolean mineUp) {
        return shouldMine(pos1, mineUp) ? pos1 : (shouldMine(pos2, mineUp) ? pos2 : null);
    }

    @Nullable
    private BlockPos nonAirOrNull(BlockPos start) {
        return isValidPos(start) ? start : null;
    }

    @Override
    public void stop() {
        if (miningPos != null) {
            setBreakProgress(miningPos, -1);
        }
        miningPos = null;
        breakTicks = 0;
    }

    @Override
    public boolean shouldContinue() {
        return shouldStartBreakingBlocks();
    }

    private boolean canBreakBlock(BlockState state) {
        return !state.isToolRequired() || mob.getMainHandStack().isSuitableFor(state);
    }

    private void setBreakProgress(BlockPos pos, int progress) {
        world.setBlockBreakingInfo(mob.getId(), pos, progress);
    }

    private boolean isValidPos(BlockPos pos) {
        var state = world.getBlockState(pos);
        return !state.isAir() && !state.canPathfindThrough(NavigationType.LAND);
    }

}
