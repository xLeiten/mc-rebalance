package me.xleiten.rebalance.util.math;

import net.minecraft.util.math.random.Random;

public class RandomHelper
{
    public static boolean chance(Random random, double percent) { return random.nextDouble() * 100 < percent; }

    public static boolean chance(Random random, int percent) { return random.nextBetween(0, 100) < percent; }

    public static double range(Random random, DoubleRange range) { return random.nextDouble() * (range.max - range.min) + range.min; }

    public static int range(Random random, IntRange range) { return random.nextBetween(range.min, range.max); }

    public static float range(Random random, FloatRange range) { return random.nextFloat() * (range.max - range.min) + range.min; }

    public static double range(Random random, double min, double max) { return random.nextDouble() * (max - min); }
}
