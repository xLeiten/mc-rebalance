package me.xleiten.rebalance.api.game.world.staged_process;

import org.jetbrains.annotations.NotNull;

public abstract class Stage<T extends ProcessContext>
{
    public final T context;

    public Stage(@NotNull T context) {
        this.context = context;
    }

    @NotNull
    public abstract TickResult<Stage<T>> tick();

    public abstract void onComplete();
}
