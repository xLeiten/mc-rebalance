package me.xleiten.rebalance.util;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.xleiten.rebalance.api.game.world.item.enchantment.ModifiedEnchantment;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

public final class EnchantmentUtils
{
    public static int countEnchantments(@NotNull Function<RegistryEntry<Enchantment>, Boolean> filter, @NotNull Map<RegistryEntry<Enchantment>, Integer> enchantments) {
        short count = 0;
        for (RegistryEntry<Enchantment> enchantment: enchantments.keySet()) {
            if (filter.apply(enchantment))
                count++;
        }
        return count;
    }

    public static Map<RegistryEntry<Enchantment>, Integer> removeConflicts(@NotNull Map<RegistryEntry<Enchantment>, Integer> enchantments, @NotNull ItemStack stack, boolean reverse) {
        var newMap = new Object2IntLinkedOpenHashMap<>(enchantments);
        if (!reverse) {
            for (RegistryEntry<Enchantment> entry : enchantments.keySet())
                checkOrRemove(newMap, entry, stack);
        } else {
            var keys = new LinkedList<>(enchantments.keySet()).descendingIterator();
            while (keys.hasNext()) {
                checkOrRemove(newMap, keys.next(), stack);
            }
        }
        return newMap;
    }

    public static <E> Map<RegistryEntry<Enchantment>, Integer> mapFrom(Iterable<E> iterable, Function<E, RegistryEntry<Enchantment>> enchantment, Function<E, Integer> level) {
        var map = new Object2IntLinkedOpenHashMap<RegistryEntry<Enchantment>>();
        iterable.forEach(it -> map.put(enchantment.apply(it), (int) level.apply(it)));
        return map;
    }

    public static Map<RegistryEntry<Enchantment>, Integer> mapFrom(@NotNull Iterable<EnchantmentLevelEntry> enchantments) {
        return mapFrom(enchantments, it -> it.enchantment.getRegistryEntry(), it -> it.level);
    }

    public static Map<RegistryEntry<Enchantment>, Integer> mapFrom(@NotNull ItemEnchantmentsComponent enchantments) {
        return mapFrom(enchantments.getEnchantmentsMap(), Map.Entry::getKey, Object2IntMap.Entry::getIntValue);
    }

    public static Map<RegistryEntry<Enchantment>, Integer> mapFrom(@NotNull ItemStack stack) {
        return mapFrom(stack.getEnchantments());
    }

    private static void checkOrRemove(@NotNull Map<RegistryEntry<Enchantment>, Integer> enchantments, @NotNull RegistryEntry<Enchantment> enchantment, @NotNull ItemStack stack) {
        if (!((ModifiedEnchantment) enchantment.value()).rebalanceMod$canAccept(enchantments, stack))
            enchantments.remove(enchantment);
    }
}
