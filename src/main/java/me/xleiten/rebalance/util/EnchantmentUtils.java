package me.xleiten.rebalance.util;

import me.xleiten.rebalance.Rebalance;
import me.xleiten.rebalance.api.game.world.item.enchantment.ModifiedEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnchantmentUtils
{
    public enum SearchMode {
        FIRST,
        LAST
    }

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

    public static int removeConflicts(ItemStack stack, SearchMode mode) {
        var enchantments = EnchantmentHelper.get(stack);
        var result = removeConflicts(enchantments, stack, mode);
        EnchantmentHelper.set(enchantments, stack);
        return result;
    }

    public static int removeConflicts(@NotNull Map<Enchantment, Integer> enchantments, @NotNull ItemStack stack, SearchMode mode) {
        var enchantsKeys = new LinkedHashSet<>(enchantments.keySet());
        int removes = 0;
        switch (mode) {
            case FIRST -> {
                for (Enchantment enchantment : enchantsKeys) {
                    if (checkOrRemove(enchantment, enchantments, stack)) removes++;
                }
            }
            case LAST -> {
                var keysIterator = new LinkedList<>(enchantsKeys).descendingIterator();
                while (keysIterator.hasNext()) {
                    if (checkOrRemove(keysIterator.next(), enchantments, stack)) removes++;
                }
            }
        }
        return removes;
    }

    public static int removeConflicts(@NotNull List<EnchantmentLevelEntry> enchantmentLevelEntries, @NotNull ItemStack stack) {
        Map<Enchantment, Integer> converted = enchantmentLevelEntries.stream().collect(Collectors.toMap(it -> it.enchantment, it -> it.level));
        var result = removeConflicts(converted, stack, SearchMode.FIRST);
        enchantmentLevelEntries.removeIf(it -> !converted.containsKey(it.enchantment));
        return result;
    }

    private static boolean checkOrRemove(Enchantment enchantment, Map<Enchantment, Integer> enchantments, ItemStack stack) {
        Rebalance.LOGGER.info("enchantment check: " + Registries.ENCHANTMENT.getId(enchantment));
        if (!((ModifiedEnchantment) enchantment).rebalanceMod$canAccept(enchantments, stack)) {
            enchantments.remove(enchantment);
            return true;
        }
        return false;
    }
}
