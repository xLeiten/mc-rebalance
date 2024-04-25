package me.xleiten.rebalance.core.mixins.world.item.multiple_protection_enchantments;

import me.xleiten.rebalance.api.game.world.item.tag.RebalanceItemTags;
import me.xleiten.rebalance.util.EnchantmentUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Function;

@Mixin(ProtectionEnchantment.class)
public abstract class MixinProtectionEnchantment extends MixinEnchantment
{
    @Unique private static final Function<Enchantment, Boolean> FILTER = (enchantment -> enchantment instanceof ProtectionEnchantment protection && protection.protectionType != ProtectionEnchantment.Type.FALL);

    @Shadow @Final public ProtectionEnchantment.Type protectionType;

    @Inject(
            method = "canAccept",
            at = @At("HEAD"),
            cancellable = true
    )
    private void changeAcceptMethod(Enchantment other, CallbackInfoReturnable<Boolean> cir) {
        if (other instanceof ProtectionEnchantment protection && this.protectionType == protection.protectionType) {
            cir.setReturnValue(false);
            return;
        }
        cir.setReturnValue(true);
    }

    @Override
    public boolean rebalanceMod$canAccept(@NotNull Map<Enchantment, Integer> enchantments, @NotNull ItemStack stack) {
        return super.rebalanceMod$canAccept(enchantments, stack) && (
                stack.isOf(Items.ENCHANTED_BOOK) || stack.isOf(Items.BOOK) ||
                this.protectionType == ProtectionEnchantment.Type.FALL ||
                EnchantmentUtils.countEnchantments(FILTER, enchantments.keySet()) <= (stack.isIn(RebalanceItemTags.CAN_HAVE_MULTIPLE_PROTECTION_ENCHANTMENTS) ? 2 : 1)
        );
    }
}
