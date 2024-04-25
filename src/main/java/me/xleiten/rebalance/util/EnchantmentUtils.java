package me.xleiten.rebalance.util;

import me.xleiten.rebalance.api.game.world.item.enchantment.ModifiedEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class EnchantmentUtils
{
    public static int countEnchantments(@NotNull Function<Enchantment, Boolean> filter, @NotNull ItemStack stack) {
        return countEnchantments(filter, EnchantmentHelper.get(stack).keySet());
    }

    public static int countEnchantments(@NotNull Function<Enchantment, Boolean> filter, @NotNull Collection<Enchantment> enchantments) {
        int counter = 0;
        for (Enchantment enchantment: enchantments) {
            if (filter.apply(enchantment))
                counter++;
        }
        return counter;
    }

    public static int removeConflicts(ItemStack stack) {
        var enchantments = EnchantmentHelper.get(stack);
        var result = removeConflicts(enchantments, stack);
        EnchantmentHelper.set(enchantments, stack);
        return result;
    }

    public static int removeConflicts(@NotNull Map<Enchantment, Integer> enchantments, @NotNull ItemStack stack) {
        var copy = new LinkedHashMap<>(enchantments);
        int removes = 0;
        for (Enchantment enchantment : copy.keySet()) {
            if (!((ModifiedEnchantment) enchantment).rebalanceMod$canAccept(enchantments, stack)) {
                enchantments.remove(enchantment);
                removes++;
            }
        }
        return removes;
    }
}
