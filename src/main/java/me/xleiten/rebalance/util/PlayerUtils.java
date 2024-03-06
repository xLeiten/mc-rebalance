package me.xleiten.rebalance.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerUtils
{
    public static void copyInventory(@NotNull ServerPlayerEntity current, @NotNull PlayerInventory inventory) {
        var playerInventory = current.getInventory();
        current.getInventory().selectedSlot = inventory.selectedSlot;
        for (int i = 0; i < inventory.size(); i++) playerInventory.setStack(i, inventory.getStack(i).copy());
        playerInventory.markDirty();
    }

    public static void sendEquipmentUpdate(@NotNull ServerPlayerEntity player) {
        //player.getServerWorld().getChunkManager().g
    }
}
