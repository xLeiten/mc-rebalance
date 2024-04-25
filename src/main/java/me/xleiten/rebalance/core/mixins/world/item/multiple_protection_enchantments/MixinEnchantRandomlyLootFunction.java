package me.xleiten.rebalance.core.mixins.world.item.multiple_protection_enchantments;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.xleiten.rebalance.api.game.world.item.enchantment.ModifiedEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantRandomlyLootFunction.class)
public abstract class MixinEnchantRandomlyLootFunction
{
    @WrapOperation(
            method = "process",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/loot/function/EnchantRandomlyLootFunction;addEnchantmentToStack(Lnet/minecraft/item/ItemStack;Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/item/ItemStack;"
            )
    )
    private ItemStack checkIfItemCanContainEnchantment(ItemStack stack, Enchantment enchantment, Random random, Operation<ItemStack> original) {
        return ((ModifiedEnchantment) enchantment).rebalanceMod$canAccept(EnchantmentHelper.get(stack), stack) ? original.call(stack, enchantment, random) : stack;
    }
}
