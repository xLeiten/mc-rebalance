package me.xleiten.rebalance.api.game.world;

import net.minecraft.nbt.NbtCompound;

public interface NbtHolder
{
    default void cringeMod$onCustomNbtDataWrite(NbtCompound nbt) { }

    default void cringeMod$onCustomNbtDataRead(NbtCompound nbt) { }
}
