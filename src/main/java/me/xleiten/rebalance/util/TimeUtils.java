package me.xleiten.rebalance.util;

import net.minecraft.util.StringHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Range;

public final class TimeUtils
{
    public static String toRealTime(@Range(from = 0, to = 24000) int ticks) {
        return StringHelper.formatTicks(ticks < 12000 ? ticks : (ticks - 12000), 20);
    }

    public static String toRealTime(World world) {
        return toRealTime((int) world.getTimeOfDay() % 24000);
    }
}
