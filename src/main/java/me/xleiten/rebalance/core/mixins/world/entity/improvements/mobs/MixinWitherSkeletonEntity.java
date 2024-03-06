package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs;

import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.util.ArmorTrimHelper;
import me.xleiten.rebalance.util.math.DoubleRange;
import me.xleiten.rebalance.util.math.IntRange;
import me.xleiten.rebalance.util.math.Range;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.item.trim.ArmorTrimPatterns;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import static me.xleiten.rebalance.Settings.WITHER_SKELETON_SETTINGS;
import static me.xleiten.rebalance.util.AttributeHelper.*;
import static me.xleiten.rebalance.util.math.RandomHelper.chance;
import static me.xleiten.rebalance.util.math.RandomHelper.range;

@Mixin(WitherSkeletonEntity.class)
public abstract class MixinWitherSkeletonEntity extends MixinHostileEntity
{
    @Unique private static final Option<DoubleRange> MOVE_SPEED_MULT = WITHER_SKELETON_SETTINGS.option("move-speed-mult", Range.create(0.25, 0.45));
    @Unique private static final Option<Double> NETHERITE_EQUIPMENT_CHANCE = WITHER_SKELETON_SETTINGS.option("netherite-equipment-chance", 0.01);
    @Unique private static final Option<Double> SWORD_SHARPNESS_CHANCE = WITHER_SKELETON_SETTINGS.option("sword-sharpness-chance", 10d);
    @Unique private static final Option<Double> SWORD_FIREASPECT_CHANCE = WITHER_SKELETON_SETTINGS.option("sword-fire-aspect-chance", 10d);
    @Unique private static final Option<IntRange> SWORD_SHARPNESS_LEVEL = WITHER_SKELETON_SETTINGS.option("sword-sharpness-level", Range.create(1, 4));
    @Unique private static final Option<IntRange> SWORD_FIREASPECT_LEVEL = WITHER_SKELETON_SETTINGS.option("sword-fireaspect-level", Range.create(1, 2));

    protected MixinWitherSkeletonEntity(EntityType<? extends HostileEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void cringeMod$onMobInitialize(ServerWorldAccess world, Random random, SpawnReason reason, Vec3d pos, Difficulty difficulty) {
        super.cringeMod$onMobInitialize(world, random, reason, pos, difficulty);
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", range(random, MOVE_SPEED_MULT.getValue())));
        var stackInHand = getStackInHand(Hand.MAIN_HAND);
        if (stackInHand.getItem() instanceof SwordItem) {
            if (chance(random, SWORD_SHARPNESS_CHANCE.getValue())) stackInHand.addEnchantment(Enchantments.SHARPNESS, range(random, SWORD_SHARPNESS_LEVEL.getValue()));
            if (chance(random, SWORD_FIREASPECT_CHANCE.getValue())) stackInHand.addEnchantment(Enchantments.FIRE_ASPECT, range(random, SWORD_FIREASPECT_LEVEL.getValue()));
        }
    }

    @Override
    public void initEquipment(Random random, LocalDifficulty localDifficulty) {
        if (chance(random, NETHERITE_EQUIPMENT_CHANCE.getValue())) {
            equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_SWORD));
            equipStack(EquipmentSlot.CHEST,
                    ArmorTrimHelper.apply(new ItemStack(Items.NETHERITE_CHESTPLATE),
                            ArmorTrimMaterials.DIAMOND,
                            ArmorTrimPatterns.RIB)
            );
            setEquipmentDropChance(EquipmentSlot.CHEST, 0.001f);
            setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.001f);
        } else
            equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
    }
}
