package me.xleiten.rebalance.api.game.event.world.entity.mob;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ServerWorldAccess;

public final class MobEntityEvents
{
    public static final Event<Initialize> INITIALIZE = EventFactory.createArrayBacked(Initialize.class, listeners -> (mob, world, reason, position) -> {
        for (Initialize listener: listeners)
            listener.initialize(mob, world, reason, position);
    });

    @FunctionalInterface
    public interface Initialize
    {
        void initialize(MobEntity mob, ServerWorldAccess world, SpawnReason reason, Vec3d position);
    }
}
