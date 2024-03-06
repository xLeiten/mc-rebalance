package me.xleiten.rebalance.api.game.world.staged_process;

import org.jetbrains.annotations.NotNull;

public abstract class Either<L, R> {

    public static <L, R> Either<L, R> left(@NotNull L value) {
        return new Left<>(value);
    }

    public static <L, R> Either<L, R> right(@NotNull R value) {
        return new Right<>(value);
    }

    public abstract L getLeft();

    public abstract R getRight();

    public static class Left<L, R> extends Either<L, R> {

        private final L left;

        Left(L left) {
            this.left = left;
        }

        @Override
        public L getLeft() {
            return left;
        }

        @Override
        public R getRight() {
            return null;
        }
    }

    public static class Right<L, R> extends Either<L, R> {

        private final R right;

        Right(R right) {
            this.right = right;
        }

        @Override
        public L getLeft() {
            return null;
        }

        @Override
        public R getRight() {
            return right;
        }
    }
}
