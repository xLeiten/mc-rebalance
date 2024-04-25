package me.xleiten.rebalance.api.game.world.item.tag;

import me.xleiten.rebalance.Rebalance;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class RebalanceItemTags
{
    public static TagKey<Item> CAN_HAVE_MULTIPLE_PROTECTION_ENCHANTMENTS = create("can_have_multiple_protection_enchantments");

    private static TagKey<Item> create(String name) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(Rebalance.MOD_ID, name));
    }
}
