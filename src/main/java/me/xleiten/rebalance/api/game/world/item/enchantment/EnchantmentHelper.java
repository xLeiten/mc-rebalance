package me.xleiten.rebalance.api.game.world.item.enchantment;

public final class EnchantmentHelper
{
    public static final String SERVER_ENCHANTMENT_NBT = "ServerEnchantments";

 /*   public static boolean contains(@NotNull ItemStack stack, @NotNull ServerEnchantment enchantment) {
        if (!stack.hasNbt()) return false;
        var nbt = stack.getOrCreateNbt();
        if (!nbt.contains(NbtKey.DISPLAY.nbt())) return false;
        var displayNbt = nbt.getCompound(NbtKey.DISPLAY.nbt());
        if (!nbt.contains(NbtKey.LORE.nbt())) return false;
        var lore = displayNbt.getList(NbtKey.LORE.nbt(), NbtElement.STRING_TYPE);
        for (NbtElement elem: lore.stream().toArray()) {
            net.minecraft.enchantment.EnchantmentHelper.getLooting()
        }
    }*/
}
