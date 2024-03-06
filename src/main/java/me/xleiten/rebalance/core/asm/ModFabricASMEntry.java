package me.xleiten.rebalance.core.asm;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public final class ModFabricASMEntry implements Runnable
{
    @Override
    public void run() {
        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();
        String name = remapper.mapClassName("intermediary", "net.minecraft.class_1886");
        ClassTinkerers.enumBuilder(name)
                .addEnumSubclass("SHIELD", "me.xleiten.rebalance.core.asm.enchantments.EnchantmentTargets$ShieldTarget")
                .addEnumSubclass("SHIELD_AND_ARMOR", "me.xleiten.rebalance.core.asm.enchantments.EnchantmentTargets$ShieldAndArmorTarget")
                .build();
    }
}
