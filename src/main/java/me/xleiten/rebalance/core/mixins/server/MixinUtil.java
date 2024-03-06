package me.xleiten.rebalance.core.mixins.server;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Util.class)
public abstract class MixinUtil
{
    @Shadow
    @Final
    static Logger LOGGER;

    @Invoker("getMaxBackgroundThreads")
    public static int getMaxBackgroundThreads() {
        throw new IllegalAccessError();
    }

    @ModifyVariable(
            method = "createWorker",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 0
    )
    private static int onModifyThreadsNumber(int i, String name) {
        return "Main".equals(name) ? (i >= 7 ? i - 3 : 1) : i;
    }

    @Inject(
            method = "method_28123",
            at = @At(
                    value = "INVOKE",
                    target = "java/util/concurrent/ForkJoinWorkerThread.setName (Ljava/lang/String;)V",
                    shift = At.Shift.AFTER
            )
    )
    private static void setPriorityOfThreadMainWorker(String string, AtomicInteger atomicInteger, ForkJoinPool pool, CallbackInfoReturnable<ForkJoinWorkerThread> cir, @Local ForkJoinWorkerThread forkJoinWorkerThread) {
        if (Objects.equals(string, "Main")) { //MAIN_WORKER_EXECUTOR
            int i = MathHelper.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, getMaxBackgroundThreads());
            int next_worker_id = atomicInteger.get() - 1;
            int halfNumWorkerMainThreads = (i >= 7 ? i - 3 : 1) / 2;
            if (next_worker_id > halfNumWorkerMainThreads)
                forkJoinWorkerThread.setPriority(1);
        }
    }
}
