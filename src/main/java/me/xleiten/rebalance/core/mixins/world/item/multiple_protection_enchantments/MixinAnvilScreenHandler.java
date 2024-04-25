package me.xleiten.rebalance.core.mixins.world.item.multiple_protection_enchantments;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.util.EnchantmentUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;

@Debug(export = true)
@Mixin(AnvilScreenHandler.class)
public abstract class MixinAnvilScreenHandler
{
    @ModifyArg(
            method = "updateResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/Property;set(I)V",
                    ordinal = 5
            )
    )
    private int recheckEnchantments(int value, @Local Map<Enchantment, Integer> enchantments, @Local(ordinal = 1) ItemStack stack) {
        return value + (stack.isEmpty() ? 0 : EnchantmentUtils.removeConflicts(enchantments, stack, EnchantmentUtils.SearchMode.FIRST));
    }
}
