package me.xleiten.rebalance.core.asm.enchantments;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ShieldItem;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

public final class EnchantmentTargets
{
    public static final EnchantmentTarget SHIELD = get("SHIELD");
    public static final EnchantmentTarget SHIELD_AND_ARMOR = get("SHIELD_AND_ARMOR");

    private static EnchantmentTarget get(@NotNull String name) {
        return ClassTinkerers.getEnum(EnchantmentTarget.class, name);
    }

    public final static class ShieldTarget extends MixinEnchantmentTarget {
        @Override
        public boolean isAcceptableItem(Item item) {
            return item instanceof ShieldItem;
        }
    }

    public final static class ShieldAndArmorTarget extends MixinEnchantmentTarget {
        @Override
        public boolean isAcceptableItem(Item item) {
            return EnchantmentTarget.ARMOR.isAcceptableItem(item) || item instanceof ShieldItem;
        }
    }

    @Mixin(EnchantmentTarget.class)
    private static abstract class MixinEnchantmentTarget {
        @Shadow
        public abstract boolean isAcceptableItem(Item item);
    }
}
