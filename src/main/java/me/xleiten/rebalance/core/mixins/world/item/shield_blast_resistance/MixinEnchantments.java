package me.xleiten.rebalance.core.mixins.world.item.shield_blast_resistance;

import me.xleiten.rebalance.api.game.world.tag.RebalanceItemTags;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Enchantments.class)
public abstract class MixinEnchantments
{
    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/Enchantment;properties(Lnet/minecraft/registry/tag/TagKey;IILnet/minecraft/enchantment/Enchantment$Cost;Lnet/minecraft/enchantment/Enchantment$Cost;I[Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/enchantment/Enchantment$Properties;",
                    ordinal = 3
            )
    )
    private static TagKey<Item> changeBlastProtectionSupportedItems(TagKey<Item> supportedItems) {
        return RebalanceItemTags.BLAST_RESISTANCE_ENCHANTABLE;
    }
}
