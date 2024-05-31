package me.xleiten.rebalance.util;

import me.xleiten.rebalance.Rebalance;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.NotNull;

public class ItemHelper
{
    public static ItemStack applyTrim(DynamicRegistryManager manager, @NotNull RegistryKey<ArmorTrimMaterial> material, @NotNull RegistryKey<ArmorTrimPattern> pattern, @NotNull ItemStack stack) {
        try {
            var materialEntry = manager.getWrapperOrThrow(RegistryKeys.TRIM_MATERIAL).getOrThrow(material);
            var patternEntry = manager.getWrapperOrThrow(RegistryKeys.TRIM_PATTERN).getOrThrow(pattern);
            stack.set(DataComponentTypes.TRIM, new ArmorTrim(materialEntry, patternEntry));
        } catch (Exception error) {
            Rebalance.LOGGER.warn("Cannot apply trim to item: {}", error.getLocalizedMessage());
        }
        return stack;
    }
}
