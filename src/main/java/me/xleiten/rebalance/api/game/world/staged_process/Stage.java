package me.xleiten.rebalance.api.game.world.staged_process;

import org.jetbrains.annotations.NotNull;

public abstract class Stage<T extends StageContext> {

    public final T context;

    public Stage(@NotNull T context) {
        this.context = context;
    }

    @NotNull
    public abstract Either<Stage<T>, Boolean> tick();

    public abstract void onComplete();

}
