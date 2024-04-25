package me.xleiten.rebalance.core.mixins.world.item.multiple_protection_enchantments;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.world.item.enchantment.ModifiedEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment implements ModifiedEnchantment
{
    @ModifyReturnValue(
            method = "isAcceptableItem",
            at = @At("TAIL")
    )
    private boolean addBookToAcceptableItems(boolean original, @Local(argsOnly = true) ItemStack stack) {
        return original || stack.isOf(Items.BOOK);
    }

    @Override
    public boolean rebalanceMod$canAccept(@NotNull Map<Enchantment, Integer> enchantments, @NotNull ItemStack stack) {
        return true;
    }
}
