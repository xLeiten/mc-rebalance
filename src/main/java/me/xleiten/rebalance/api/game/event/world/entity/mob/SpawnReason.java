package me.xleiten.rebalance.api.game.event.world.entity.mob;

import org.jetbrains.annotations.NotNull;

public enum SpawnReason
{
    NATURAL,
    CHUNK_GENERATION,
    SPAWNER,
    STRUCTURE,
    BREEDING,
    MOB_SUMMONED,
    JOCKEY,
    EVENT,
    CONVERSION,
    REINFORCEMENT,
    TRIGGERED,
    BUCKET,
    SPAWN_EGG,
    COMMAND,
    DISPENSER,
    PATROL,
    TRIAL_SPAWNER,

    BLOCK_CHANGE,
    GOLEM,
    BLOCK,
    PROJECTILE,
    DUPLICATE,

    GENERIC;

    public static SpawnReason from(@NotNull net.minecraft.entity.SpawnReason reason) {
        for (SpawnReason res: values())
            if (res.name().equals(reason.name()))
                return res;

        return GENERIC;
    }

    public static SpawnReason from(@NotNull String entryName) {
        for (SpawnReason res: values())
            if (res.name().equals(entryName))
                return res;

        return GENERIC;
    }
}

