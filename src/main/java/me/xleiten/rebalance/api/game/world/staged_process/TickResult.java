package me.xleiten.rebalance.api.game.world.staged_process;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class TickResult<L extends Stage<?>>
{
    public static <L extends Stage<?>> Left<L> stage(@NotNull L value) {
        return new Left<>(value);
    }

    public static <L extends Stage<?>> Right<L> complete(boolean value) {
        return new Right<>(value);
    }

    @Nullable
    public abstract L stage();

    @Nullable
    public abstract Boolean result();

    public static class Left<L extends Stage<?>> extends TickResult<L>
    {
        private final L left;

        Left(L left) {
            this.left = left;
        }

        @Override
        public L stage() {
            return left;
        }

        @Override
        public Boolean result() {
            return null;
        }
    }

    public static class Right<T extends Stage<?>> extends TickResult<T>
    {
        private final Boolean value;

        Right(Boolean value) {
            this.value = value;
        }

        @Override
        public T stage() {
            return null;
        }

        @Override
        public Boolean result() {
            return value;
        }
    }
}
