package me.xleiten.rebalance.util.math;

public abstract class ValueRange<T>
{
    public final T min;
    public final T max;

    public ValueRange(T min, T max) {
        this.min = min;
        this.max = max;
    }

    public abstract boolean isIn(T value);
}
