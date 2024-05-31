package me.xleiten.rebalance.core.datagen;

import me.xleiten.rebalance.api.game.world.tag.RebalanceBiomeTags;
import me.xleiten.rebalance.api.game.world.tag.RebalanceDamageTypeTags;
import me.xleiten.rebalance.api.game.world.tag.RebalanceEntityTypeTags;
import me.xleiten.rebalance.api.game.world.tag.RebalanceItemTags;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.concurrent.CompletableFuture;

public final class RebalanceDataGenerator implements DataGeneratorEntrypoint
{
    public RebalanceDataGenerator() {}

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator)
    {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(EntityTagProvider::new);
        pack.addProvider(BiomeTagProvider::new);
        pack.addProvider(ItemTagProvider::new);
        pack.addProvider(DamageTypesProvider::new);
    }

    private static class DamageTypesProvider extends FabricTagProvider<DamageType>
    {
        public DamageTypesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            getOrCreateTagBuilder(RebalanceDamageTypeTags.SUMMONS_ZOMBIE_REINFORCEMENT)
                    .add(
                            DamageTypes.MOB_ATTACK,
                            DamageTypes.MOB_PROJECTILE,
                            DamageTypes.MOB_ATTACK_NO_AGGRO,
                            DamageTypes.PLAYER_ATTACK,
                            DamageTypes.PLAYER_EXPLOSION,
                            DamageTypes.SONIC_BOOM,
                            DamageTypes.WITHER_SKULL,
                            DamageTypes.INDIRECT_MAGIC,
                            DamageTypes.STING,
                            DamageTypes.THORNS
                    );
        }
    }

    private static class ItemTagProvider extends FabricTagProvider.ItemTagProvider
    {

        public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            getOrCreateTagBuilder(RebalanceItemTags.CAN_HAVE_MULTIPLE_PROTECTION_ENCHANTMENTS)
                    .add(
                            Items.NETHERITE_CHESTPLATE,
                            Items.NETHERITE_HELMET,
                            Items.NETHERITE_LEGGINGS,
                            Items.NETHERITE_BOOTS
                    );

            getOrCreateTagBuilder(RebalanceItemTags.BLAST_RESISTANCE_ENCHANTABLE)
                    .forceAddTag(ItemTags.ARMOR_ENCHANTABLE)
                    .add(Items.SHIELD);
        }
    }

    private static class EntityTagProvider extends FabricTagProvider.EntityTypeTagProvider
    {
        public EntityTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture)
        {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            getOrCreateTagBuilder(RebalanceEntityTypeTags.BOSSES)
                    .add(
                            EntityType.WITHER,
                            EntityType.ENDER_DRAGON
                    )
            ;

            getOrCreateTagBuilder(RebalanceEntityTypeTags.GUARDIANS)
                    .add(
                            EntityType.GUARDIAN,
                            EntityType.ELDER_GUARDIAN
                    )
            ;

            getOrCreateTagBuilder(RebalanceEntityTypeTags.GOLEMS)
                    .add(
                            EntityType.IRON_GOLEM,
                            EntityType.SNOW_GOLEM,
                            EntityType.SHULKER
                    )
            ;

            getOrCreateTagBuilder(RebalanceEntityTypeTags.CANNOT_REGEN_HEALTH)
                    .forceAddTag(EntityTypeTags.SKELETONS)
                    .addTag(RebalanceEntityTypeTags.BOSSES)
                    .addTag(RebalanceEntityTypeTags.GUARDIANS)
                    .addTag(RebalanceEntityTypeTags.GOLEMS)
                    .add(
                            EntityType.ZOMBIE,
                            EntityType.ZOMBIE_VILLAGER,
                            EntityType.ZOMBIFIED_PIGLIN,
                            EntityType.ZOGLIN,
                            EntityType.HUSK,
                            EntityType.VEX,
                            EntityType.ZOMBIE_HORSE,
                            EntityType.PHANTOM,
                            EntityType.GIANT
                    )
            ;

            getOrCreateTagBuilder(RebalanceEntityTypeTags.NOT_AFFECTED_BY_HEALTH_SLOWDOWN)
                    .addTag(RebalanceEntityTypeTags.BOSSES)
                    .addTag(RebalanceEntityTypeTags.GUARDIANS)
            ;

            getOrCreateTagBuilder(RebalanceEntityTypeTags.CAN_MINE_BLOCKS)
                    .add(
                            EntityType.ZOMBIE,
                            EntityType.HUSK,
                            EntityType.SKELETON,
                            EntityType.WITHER_SKELETON,
                            EntityType.ZOMBIFIED_PIGLIN,
                            EntityType.PIGLIN,
                            EntityType.STRAY,
                            EntityType.ENDERMAN
                    )
            ;

            getOrCreateTagBuilder(RebalanceEntityTypeTags.WITHOUT_HEALTH_DISPLAY)
                    .addTag(RebalanceEntityTypeTags.BOSSES)
            ;

            getOrCreateTagBuilder(RebalanceEntityTypeTags.CAN_FULLY_SEE_IN_DARKNESS)
                    .forceAddTag(EntityTypeTags.SKELETONS)
                    .addTag(RebalanceEntityTypeTags.GOLEMS)
                    .addTag(RebalanceEntityTypeTags.BOSSES)
                    .addTag(RebalanceEntityTypeTags.GUARDIANS)
                    .add(
                            EntityType.WARDEN,
                            EntityType.ENDERMAN,
                            EntityType.WITCH,
                            EntityType.CREEPER
                    )
            ;

            getOrCreateTagBuilder(RebalanceEntityTypeTags.VISION_NOT_AFFECTED_BY_SCULK)
                    .add(
                            EntityType.WARDEN
                    )
            ;
        }
    }

    private static class BiomeTagProvider extends FabricTagProvider<Biome>
    {
        /**
         * Constructs a new {@link FabricTagProvider} with the default computed path.
         *
         * <p>Common implementations of this class are provided.
         *
         * @param output           the {@link FabricDataOutput} instance
         * @param registriesFuture the backing registry for the tag type
         */
        public BiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.BIOME, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            getOrCreateTagBuilder(RebalanceBiomeTags.SCULK_BIOME)
                    .add(
                            BiomeKeys.DEEP_DARK
                    )
            ;
        }
    }
}
