package me.xleiten.rebalance.api.game.world.entity.mob;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.Arrays;
import java.util.List;

public enum SpiderType {

  CAVE("cave"),

  JUNGLE(
          "jungle",
          BiomeKeys.JUNGLE,
          BiomeKeys.BAMBOO_JUNGLE,
          BiomeKeys.SPARSE_JUNGLE,
          BiomeKeys.SAVANNA,
          BiomeKeys.SAVANNA_PLATEAU,
          BiomeKeys.WINDSWEPT_SAVANNA
  ),

  DARK_FOREST(
          "dark_forest",
          BiomeKeys.DARK_FOREST
  ),

  DESERT(
          "desert",
          BiomeKeys.DESERT,
          BiomeKeys.BADLANDS,
          BiomeKeys.ERODED_BADLANDS,
          BiomeKeys.WOODED_BADLANDS
  ),

  NORMAL("normal"),
  ;

  public final String name;
  private final List<RegistryKey<Biome>> biomes;

  @SafeVarargs
  SpiderType(String name, RegistryKey<Biome>... biomes) {
    this.biomes = Arrays.asList(biomes);
    this.name = name;
  }

  public static SpiderType fromName(String name) {
    for (SpiderType type: SpiderType.values())
      if (type.name.equals(name))
        return type;

    return NORMAL;
  }

  public static SpiderType forBiome(RegistryKey<Biome> biome) {
    for (SpiderType type: SpiderType.values())
      if (type.biomes.contains(biome))
        return type;

    return NORMAL;
  }

}
