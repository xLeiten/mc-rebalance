package me.xleiten.rebalance.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

public class ArmorTrimHelper
{
    public static ItemStack apply(@NotNull ItemStack stack, RegistryKey<ArmorTrimMaterial> material, RegistryKey<ArmorTrimPattern> pattern) {
        var trimNbt = new NbtCompound();
        trimNbt.putString("material", material.getValue().toString());
        trimNbt.putString("pattern", pattern.getValue().toString());
        stack.getOrCreateNbt().put("Trim", trimNbt);
        return stack;
    }
}
