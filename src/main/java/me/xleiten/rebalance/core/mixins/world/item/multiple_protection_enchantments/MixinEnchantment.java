package me.xleiten.rebalance.core.mixins.world.item.multiple_protection_enchantments;

import me.xleiten.rebalance.api.game.world.item.enchantment.ModifiedEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment implements ModifiedEnchantment
{
    @Shadow @Deprecated public abstract RegistryEntry.Reference<Enchantment> getRegistryEntry();

    @Override
    public boolean rebalanceMod$canAccept(@NotNull Map<RegistryEntry<Enchantment>, Integer> enchantments, @NotNull ItemStack stack) {
        return true;
    }
}
