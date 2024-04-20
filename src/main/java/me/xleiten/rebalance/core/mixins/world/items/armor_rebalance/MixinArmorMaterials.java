package me.xleiten.rebalance.core.mixins.world.items.armor_rebalance;

import net.minecraft.item.ArmorMaterials;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static me.xleiten.rebalance.core.game.item.armor.ArmorBuffsOptions.*;

@Debug(export = true)
@Mixin(ArmorMaterials.class)
public abstract class MixinArmorMaterials
{
    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ArmorMaterials;<init>(Ljava/lang/String;ILjava/lang/String;ILjava/util/EnumMap;ILnet/minecraft/sound/SoundEvent;FFLjava/util/function/Supplier;)V",
                    ordinal = 1
            ),
            index = 7
    )
    private static float changeChainmailArmorToughness(float par8) {
        return CHAINMAIL_TOUGHNESS.getValue();
    }

    @ModifyArgs(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ArmorMaterials;<init>(Ljava/lang/String;ILjava/lang/String;ILjava/util/EnumMap;ILnet/minecraft/sound/SoundEvent;FFLjava/util/function/Supplier;)V",
                    ordinal = 2
            )
    )
    private static void changeIronArmorProperties(Args args) {
        args.set(7, IRON_TOUGHNESS.getValue());
        args.set(8, IRON_KNOCKBACK_RESISTANCE.getValue());
    }

    @ModifyArgs(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ArmorMaterials;<init>(Ljava/lang/String;ILjava/lang/String;ILjava/util/EnumMap;ILnet/minecraft/sound/SoundEvent;FFLjava/util/function/Supplier;)V",
                    ordinal = 4
            )
    )
    private static void changeDiamondArmorProperties(Args args) {
        args.set(7, DIAMOND_TOUGHNESS.getValue());
        args.set(8, DIAMOND_KNOCKBACK_RESISTANCE.getValue());
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ArmorMaterials;<init>(Ljava/lang/String;ILjava/lang/String;ILjava/util/EnumMap;ILnet/minecraft/sound/SoundEvent;FFLjava/util/function/Supplier;)V",
                    ordinal = 6
            ),
            index = 7
    )
    private static float changeNetheriteArmorToughness(float par8) {
        return NETHERITE_TOUGHNESS.getValue();
    }


    // Netherite armor points changes
    @ModifyArgs(
            method = "method_48406",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/EnumMap;put(Ljava/lang/Enum;Ljava/lang/Object;)Ljava/lang/Object;",
                    ordinal = 0
            )
    )
    private static void changeNetheriteArmorPointsForBoots(Args args) {
        args.set(1, 4);
    }

    @ModifyArgs(
            method = "method_48406",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/EnumMap;put(Ljava/lang/Enum;Ljava/lang/Object;)Ljava/lang/Object;",
                    ordinal = 1
            )
    )
    private static void changeNetheriteArmorPointsForLeggings(Args args) {
        args.set(1, 7);
    }

    @ModifyArgs(
            method = "method_48406",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/EnumMap;put(Ljava/lang/Enum;Ljava/lang/Object;)Ljava/lang/Object;",
                    ordinal = 2
            )
    )
    private static void changeNetheriteArmorPointsForChestplate(Args args) {
        args.set(1, 10);
    }

    @ModifyArgs(
            method = "method_48406",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/EnumMap;put(Ljava/lang/Enum;Ljava/lang/Object;)Ljava/lang/Object;",
                    ordinal = 3
            )
    )
    private static void changeNetheriteArmorPointsForHelmet(Args args) {
        args.set(1, 4);
    }
}
