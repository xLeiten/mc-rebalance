package me.xleiten.rebalance.util.math;

public class Range
{
    public static IntRange create(int min, int max) {
        return new IntRange(Math.min(min, max), Math.max(min, max));
    }

    public static DoubleRange create(double min, double max) {
        return new DoubleRange(Math.min(min, max), Math.max(min, max));
    }

    public static FloatRange create(float min, float max) {
        return new FloatRange(Math.min(min, max), Math.max(min, max));
    }
}
