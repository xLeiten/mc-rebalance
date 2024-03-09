package me.xleiten.rebalance.core.datagen;

import me.xleiten.rebalance.api.game.world.entity.tags.ModEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public final class RebalanceDataGenerator implements DataGeneratorEntrypoint
{
    public RebalanceDataGenerator() {}

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator)
    {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(EntityTagProvider::new);
    }

    private static class EntityTagProvider extends FabricTagProvider.EntityTypeTagProvider
    {
        public EntityTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture)
        {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            getOrCreateTagBuilder(ModEntityTypeTags.BOSSES)
                    .add(
                            EntityType.WITHER,
                            EntityType.ENDER_DRAGON
                    )
            ;

            getOrCreateTagBuilder(ModEntityTypeTags.GUARDIANS)
                    .add(
                            EntityType.GUARDIAN,
                            EntityType.ELDER_GUARDIAN
                    )
            ;

            getOrCreateTagBuilder(ModEntityTypeTags.GOLEMS)
                    .add(
                            EntityType.IRON_GOLEM,
                            EntityType.SNOW_GOLEM,
                            EntityType.SHULKER
                    )
            ;

            getOrCreateTagBuilder(ModEntityTypeTags.CANNOT_REGEN_HEALTH)
                    .forceAddTag(EntityTypeTags.SKELETONS)
                    .addTag(ModEntityTypeTags.BOSSES)
                    .addTag(ModEntityTypeTags.GUARDIANS)
                    .addTag(ModEntityTypeTags.GOLEMS)
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

            getOrCreateTagBuilder(ModEntityTypeTags.NOT_AFFECTED_BY_HEALTH_SLOWDOWN)
                    .addTag(ModEntityTypeTags.BOSSES)
                    .addTag(ModEntityTypeTags.GUARDIANS)
            ;

            getOrCreateTagBuilder(ModEntityTypeTags.CAN_MINE_BLOCKS)
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

            getOrCreateTagBuilder(ModEntityTypeTags.WITHOUT_HEALTH_DISPLAY)
                    .addTag(ModEntityTypeTags.BOSSES)
            ;
        }
    }
}
