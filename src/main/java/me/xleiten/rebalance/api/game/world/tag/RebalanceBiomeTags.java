package me.xleiten.rebalance.api.game.world.tag;

import me.xleiten.rebalance.Rebalance;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public final class RebalanceBiomeTags
{
    public static final TagKey<Biome> SCULK_BIOME = create("sculk_biome");

    private static TagKey<Biome> create(String name) {
        return TagKey.of(RegistryKeys.BIOME, new Identifier(Rebalance.MOD_ID, name));
    }
}
