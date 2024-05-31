package me.xleiten.rebalance.core.mixins.world.item.armor_rebalance;

import me.xleiten.rebalance.Settings;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.EnumMap;

@Debug(export = true)
@Mixin(ArmorMaterials.class)
public abstract class MixinArmorMaterials
{
    @ModifyArgs(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ArmorMaterials;register(Ljava/lang/String;Ljava/util/EnumMap;ILnet/minecraft/registry/entry/RegistryEntry;FFLjava/util/function/Supplier;Ljava/util/List;)Lnet/minecraft/registry/entry/RegistryEntry;"
            )
    )
    private static void changeLeatherArmor(Args args) {
        var leather = Settings.ITEM_ARMOR__MATERIAL_LEATHER.value();
        args.set(1, Util.make(new EnumMap<ArmorItem.Type, Integer>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, leather.bootsArmor());
            map.put(ArmorItem.Type.LEGGINGS, leather.legsArmor());
            map.put(ArmorItem.Type.CHESTPLATE, leather.chestArmor());
            map.put(ArmorItem.Type.HELMET, leather.helmArmor());
            map.put(ArmorItem.Type.BODY, leather.bodyArmor());
        }));
        args.set(2, leather.enchantability());
        args.set(4, leather.toughness());
        args.set(5, leather.knockbackResistance());
    }

    @ModifyArgs(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ArmorMaterials;register(Ljava/lang/String;Ljava/util/EnumMap;ILnet/minecraft/registry/entry/RegistryEntry;FFLjava/util/function/Supplier;)Lnet/minecraft/registry/entry/RegistryEntry;",
                    ordinal = 0
            )
    )
    private static void changeChainArmor(Args args) {
        var chain = Settings.ITEM_ARMOR__MATERIAL_CHAIN.value();
        args.set(1, Util.make(new EnumMap<ArmorItem.Type, Integer>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, chain.bootsArmor());
            map.put(ArmorItem.Type.LEGGINGS, chain.legsArmor());
            map.put(ArmorItem.Type.CHESTPLATE, chain.chestArmor());
            map.put(ArmorItem.Type.HELMET, chain.helmArmor());
            map.put(ArmorItem.Type.BODY, chain.bodyArmor());
        }));
        args.set(2, chain.enchantability());
        args.set(4, chain.toughness());
        args.set(5, chain.knockbackResistance());
    }

    @ModifyArgs(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ArmorMaterials;register(Ljava/lang/String;Ljava/util/EnumMap;ILnet/minecraft/registry/entry/RegistryEntry;FFLjava/util/function/Supplier;)Lnet/minecraft/registry/entry/RegistryEntry;",
                    ordinal = 1
            )
    )
    private static void changeIronArmor(Args args) {
        var iron = Settings.ITEM_ARMOR__MATERIAL_IRON.value();
        args.set(1, Util.make(new EnumMap<ArmorItem.Type, Integer>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, iron.bootsArmor());
            map.put(ArmorItem.Type.LEGGINGS, iron.legsArmor());
            map.put(ArmorItem.Type.CHESTPLATE, iron.chestArmor());
            map.put(ArmorItem.Type.HELMET, iron.helmArmor());
            map.put(ArmorItem.Type.BODY, iron.bodyArmor());
        }));
        args.set(2, iron.enchantability());
        args.set(4, iron.toughness());
        args.set(5, iron.knockbackResistance());
    }

    @ModifyArgs(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ArmorMaterials;register(Ljava/lang/String;Ljava/util/EnumMap;ILnet/minecraft/registry/entry/RegistryEntry;FFLjava/util/function/Supplier;)Lnet/minecraft/registry/entry/RegistryEntry;",
                    ordinal = 2
            )
    )
    private static void changeGoldArmor(Args args) {
        var gold = Settings.ITEM_ARMOR__MATERIAL_GOLD.value();
        args.set(1, Util.make(new EnumMap<ArmorItem.Type, Integer>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, gold.bootsArmor());
            map.put(ArmorItem.Type.LEGGINGS, gold.legsArmor());
            map.put(ArmorItem.Type.CHESTPLATE, gold.chestArmor());
            map.put(ArmorItem.Type.HELMET, gold.helmArmor());
            map.put(ArmorItem.Type.BODY, gold.bodyArmor());
        }));
        args.set(2, gold.enchantability());
        args.set(4, gold.toughness());
        args.set(5, gold.knockbackResistance());
    }

    @ModifyArgs(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ArmorMaterials;register(Ljava/lang/String;Ljava/util/EnumMap;ILnet/minecraft/registry/entry/RegistryEntry;FFLjava/util/function/Supplier;)Lnet/minecraft/registry/entry/RegistryEntry;",
                    ordinal = 3
            )
    )
    private static void changeDiamondArmor(Args args) {
        var diamond = Settings.ITEM_ARMOR__MATERIAL_DIAMOND.value();
        args.set(1, Util.make(new EnumMap<ArmorItem.Type, Integer>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, diamond.bootsArmor());
            map.put(ArmorItem.Type.LEGGINGS, diamond.legsArmor());
            map.put(ArmorItem.Type.CHESTPLATE, diamond.chestArmor());
            map.put(ArmorItem.Type.HELMET, diamond.helmArmor());
            map.put(ArmorItem.Type.BODY, diamond.bodyArmor());
        }));
        args.set(2, diamond.enchantability());
        args.set(4, diamond.toughness());
        args.set(5, diamond.knockbackResistance());
    }

    @ModifyArgs(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ArmorMaterials;register(Ljava/lang/String;Ljava/util/EnumMap;ILnet/minecraft/registry/entry/RegistryEntry;FFLjava/util/function/Supplier;)Lnet/minecraft/registry/entry/RegistryEntry;",
                    ordinal = 4
            )
    )
    private static void changeTurtleArmor(Args args) {
        var turtle = Settings.ITEM_ARMOR__MATERIAL_TURTLE.value();
        args.set(1, Util.make(new EnumMap<ArmorItem.Type, Integer>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, turtle.bootsArmor());
            map.put(ArmorItem.Type.LEGGINGS, turtle.legsArmor());
            map.put(ArmorItem.Type.CHESTPLATE, turtle.chestArmor());
            map.put(ArmorItem.Type.HELMET, turtle.helmArmor());
            map.put(ArmorItem.Type.BODY, turtle.bodyArmor());
        }));
        args.set(2, turtle.enchantability());
        args.set(4, turtle.toughness());
        args.set(5, turtle.knockbackResistance());
    }

    @ModifyArgs(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ArmorMaterials;register(Ljava/lang/String;Ljava/util/EnumMap;ILnet/minecraft/registry/entry/RegistryEntry;FFLjava/util/function/Supplier;)Lnet/minecraft/registry/entry/RegistryEntry;",
                    ordinal = 5
            )
    )
    private static void changeNetheriteArmor(Args args) {
        var netherite = Settings.ITEM_ARMOR__MATERIAL_NETHERITE.value();
        args.set(1, Util.make(new EnumMap<ArmorItem.Type, Integer>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, netherite.bootsArmor());
            map.put(ArmorItem.Type.LEGGINGS, netherite.legsArmor());
            map.put(ArmorItem.Type.CHESTPLATE, netherite.chestArmor());
            map.put(ArmorItem.Type.HELMET, netherite.helmArmor());
            map.put(ArmorItem.Type.BODY, netherite.bodyArmor());
        }));
        args.set(2, netherite.enchantability());
        args.set(4, netherite.toughness());
        args.set(5, netherite.knockbackResistance());
    }

    @ModifyArgs(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ArmorMaterials;register(Ljava/lang/String;Ljava/util/EnumMap;ILnet/minecraft/registry/entry/RegistryEntry;FFLjava/util/function/Supplier;)Lnet/minecraft/registry/entry/RegistryEntry;",
                    ordinal = 6
            )
    )
    private static void changeArmadilloArmor(Args args) {
        var armadillo = Settings.ITEM_ARMOR__MATERIAL_ARMADILLO.value();
        args.set(1, Util.make(new EnumMap<ArmorItem.Type, Integer>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, armadillo.bootsArmor());
            map.put(ArmorItem.Type.LEGGINGS, armadillo.legsArmor());
            map.put(ArmorItem.Type.CHESTPLATE, armadillo.chestArmor());
            map.put(ArmorItem.Type.HELMET, armadillo.helmArmor());
            map.put(ArmorItem.Type.BODY, armadillo.bodyArmor());
        }));
        args.set(2, armadillo.enchantability());
        args.set(4, armadillo.toughness());
        args.set(5, armadillo.knockbackResistance());
    }
}
