package me.xleiten.rebalance.api.game.world.item.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface ModifiedEnchantment
{
    boolean rebalanceMod$canAccept(@NotNull Map<RegistryEntry<Enchantment>, Integer> enchantments, @NotNull ItemStack stack);
}
