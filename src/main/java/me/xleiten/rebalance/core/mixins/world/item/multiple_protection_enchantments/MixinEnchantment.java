package me.xleiten.rebalance.core.mixins.world.item.multiple_protection_enchantments;

import me.xleiten.rebalance.api.game.world.item.enchantment.ModifiedEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Map;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment implements ModifiedEnchantment
{
    @Override
    public boolean rebalanceMod$canAccept(@NotNull Map<Enchantment, Integer> enchantments, @NotNull ItemStack stack) {
        return true;
    }
}
