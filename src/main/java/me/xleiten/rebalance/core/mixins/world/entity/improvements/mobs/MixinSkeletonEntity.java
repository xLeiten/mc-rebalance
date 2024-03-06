package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs;

import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.util.math.DoubleRange;
import me.xleiten.rebalance.util.math.RandomHelper;
import me.xleiten.rebalance.util.math.Range;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.util.AttributeHelper.*;
import static me.xleiten.rebalance.util.math.RandomHelper.chance;

@Mixin(SkeletonEntity.class)
public abstract class MixinSkeletonEntity extends MixinHostileEntity
{
    @Unique private static final Option<DoubleRange> MOVE_SPEED_MULT = SKELETON_SETTINGS.option("move-speed-mult", Range.create(0.15, 0.35));
    @Unique private static final Option<Double> USE_SWORD_CHANCE = SKELETON_SETTINGS.option("use-sword-chance", 3d);
    @Unique private static final Option<Double> DIAMOND_SWORD_CHANCE = SKELETON_SETTINGS.option("diamond-sword-chance", 0.01d);

    protected MixinSkeletonEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void cringeMod$onMobInitialize(ServerWorldAccess world, Random random, SpawnReason reason, Vec3d pos, Difficulty difficulty) {
        super.cringeMod$onMobInitialize(world, random, reason, pos, difficulty);
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", RandomHelper.range(random, MOVE_SPEED_MULT.getValue())));
        if (chance(random, USE_SWORD_CHANCE.getValue())) {
            Item weapon;
            if (chance(random, DIAMOND_SWORD_CHANCE.getValue())) {
                weapon = Items.DIAMOND_SWORD;
                setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.005f);
            } else {
                weapon = Items.IRON_SWORD;
            }
            setStackInHand(Hand.MAIN_HAND, new ItemStack(weapon));
        }
    }
}
