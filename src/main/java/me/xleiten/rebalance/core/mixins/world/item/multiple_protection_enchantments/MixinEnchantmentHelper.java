package me.xleiten.rebalance.core.mixins.world.item.multiple_protection_enchantments;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.xleiten.rebalance.api.game.world.item.enchantment.ModifiedEnchantment;
import me.xleiten.rebalance.util.EnchantmentUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper
{

    @WrapOperation(
            method = "enchant",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;addEnchantment(Lnet/minecraft/enchantment/Enchantment;I)V"
            )
    )
    private static void checkApplicableEnchantment1(ItemStack stack, Enchantment enchantment, int level, Operation<Void> original) {
        if (((ModifiedEnchantment) enchantment).rebalanceMod$canAccept(EnchantmentHelper.get(stack), stack)) original.call(stack, enchantment, level);
    }

    @WrapOperation(
            method = "enchant",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/EnchantedBookItem;addEnchantment(Lnet/minecraft/item/ItemStack;Lnet/minecraft/enchantment/EnchantmentLevelEntry;)V"
            )
    )
    private static void checkApplicableEnchantment2(ItemStack stack, EnchantmentLevelEntry entry, Operation<Void> original) {
        if (((ModifiedEnchantment) entry.enchantment).rebalanceMod$canAccept(EnchantmentHelper.get(stack), stack)) original.call(stack, entry);
    }
}
