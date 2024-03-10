package me.xleiten.rebalance.util;

import net.minecraft.util.StringHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Range;

import java.util.Locale;

public class TimeUtils
{
    public static String toRealTime(int ticks) {
        return StringHelper.formatTicks(ticks, 20);
    }

    public static String toRealTime(World world) {
        return toRealTime((int) (world.getTimeOfDay() + 6000) % 24000);
    }

    public static String format(World world, @Range(from = 0, to = 24000) int ticksDelta) {
        var ticks = (world.getTimeOfDay() + ticksDelta) % 24000;
        var minute = ticks / 1000;
        var seconds = ((double) ticks - minute * 1000d) / 1000d * 60d;
        return String.format(Locale.ROOT, "%02d:%02d", minute, (int) seconds);
    }
}
