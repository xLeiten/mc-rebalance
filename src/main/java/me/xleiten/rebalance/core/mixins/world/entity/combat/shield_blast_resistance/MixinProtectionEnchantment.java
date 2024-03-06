package me.xleiten.rebalance.core.mixins.world.entity.combat.shield_blast_resistance;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.core.asm.enchantments.EnchantmentTargets;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ProtectionEnchantment.class)
public abstract class MixinProtectionEnchantment extends Enchantment
{
    protected MixinProtectionEnchantment(Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes)
    {
        super(rarity, target, slotTypes);
    }

    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/Enchantment;<init>(Lnet/minecraft/enchantment/Enchantment$Rarity;Lnet/minecraft/enchantment/EnchantmentTarget;[Lnet/minecraft/entity/EquipmentSlot;)V"
            ),
            index = 1
    )
    private static EnchantmentTarget changeTargetForBlastResistance(EnchantmentTarget target, @Local(argsOnly = true) ProtectionEnchantment.Type type) {
        return type == ProtectionEnchantment.Type.EXPLOSION ? EnchantmentTargets.SHIELD_AND_ARMOR : (type == ProtectionEnchantment.Type.FALL ? EnchantmentTarget.ARMOR_FEET : EnchantmentTarget.ARMOR);
    }
}
