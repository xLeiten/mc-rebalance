package me.xleiten.rebalance.core.mixins.world.item.multiple_protection_enchantments;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.xleiten.rebalance.util.EnchantmentUtils;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;

@Mixin(AnvilScreenHandler.class)
public abstract class MixinAnvilScreenHandler
{
    @WrapOperation(
            method = "updateResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/EnchantmentHelper;getEnchantments(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/component/type/ItemEnchantmentsComponent;",
                    ordinal = 0
            )
    )
    private ItemEnchantmentsComponent saveEnchantmentsToOrderedMap(ItemStack stack, Operation<ItemEnchantmentsComponent> original, @Share("enchantments_ordered") LocalRef<Map<RegistryEntry<Enchantment>, Integer>> shared) {
        var result = original.call(stack);
        shared.set(EnchantmentUtils.mapFrom(result));
        return result;
    }

    @WrapOperation(
            method = "updateResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/component/type/ItemEnchantmentsComponent$Builder;set(Lnet/minecraft/enchantment/Enchantment;I)V"
            )
    )
    private void addEnchantmentsToShared(ItemEnchantmentsComponent.Builder instance, Enchantment enchantment, int level, Operation<Void> original, @Share("enchantments_ordered") LocalRef<Map<RegistryEntry<Enchantment>, Integer>> shared) {
        original.call(instance, enchantment, level);
        shared.get().put(enchantment.getRegistryEntry(), level);
    }

    @ModifyArg(
            method = "updateResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/Property;set(I)V",
                    ordinal = 5
            )
    )
    private int recheckEnchantments(int value, @Local ItemEnchantmentsComponent.Builder builder, @Local(ordinal = 1) ItemStack stack, @Share("enchantments_ordered") LocalRef<Map<RegistryEntry<Enchantment>, Integer>> shared) {
        var result = EnchantmentUtils.removeConflicts(shared.get(), stack, false);
        var size = builder.getEnchantments().size();
        builder.remove(it -> !result.containsKey(it));
        return value + (stack.isEmpty() ? 0 : size - result.size());
    }
}
