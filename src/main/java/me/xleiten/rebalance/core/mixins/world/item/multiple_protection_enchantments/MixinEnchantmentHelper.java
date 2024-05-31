package me.xleiten.rebalance.core.mixins.world.item.multiple_protection_enchantments;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.util.EnchantmentUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper
{
    @ModifyReturnValue(
            method = "generateEnchantments",
            at = @At("RETURN")
    )
    private static List<EnchantmentLevelEntry> changeEnchantmentGeneration(List<EnchantmentLevelEntry> original, @Local(argsOnly = true) ItemStack stack) {
        var result = EnchantmentUtils.removeConflicts(EnchantmentUtils.mapFrom(original), stack, false);
        original.removeIf(it -> !result.containsKey(it.enchantment.getRegistryEntry()));
        return original;
    }
}
