package me.xleiten.rebalance.api.game.world.staged_process;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public final class StagedProcess<T extends StageContext>
{
    private final BiConsumer<Boolean, T> onComplete;
    private final T context;
    private Stage<T> stage;

    private boolean completed = false;

    public StagedProcess(@NotNull Stage<T> initial, @NotNull BiConsumer<Boolean, T> onComplete)
    {
        this.onComplete = onComplete;
        this.context = initial.context;
        this.stage = initial;
    }

    public void tick() {
        if (!completed) {
            var tickResult = stage.tick();
            var left = tickResult.stage();
            if (left != null) {
                if (stage != left) {
                    stage.onComplete();
                    stage = left;
                }
            } else {
                complete();
                onComplete.accept(tickResult.result(), context);
            }
        }
    }

    private void complete() {
        if (stage != null) stage.onComplete();
        completed = true;
    }

    public void forceStop() {
        complete();
        onComplete.accept(false, context);
    }

    public T getContext() {
        return context;
    }

    public Stage<T> getCurrentStage() {
        return stage;
    }

    public boolean isCompleted() {
        return completed;
    }
}
