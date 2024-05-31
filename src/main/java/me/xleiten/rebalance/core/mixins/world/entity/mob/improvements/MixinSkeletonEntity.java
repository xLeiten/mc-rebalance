package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.math.RandomHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.api.math.RandomHelper.chance;
import static me.xleiten.rebalance.util.AttributeHelper.addModifier;
import static me.xleiten.rebalance.util.AttributeHelper.modifier;

@Mixin(SkeletonEntity.class)
public abstract class MixinSkeletonEntity extends MixinHostileEntity
{
    protected MixinSkeletonEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason) {
        super.rebalanceMod$onFirstSpawn(world, random, reason);
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", RandomHelper.range(random, MOB_SKELETON__MOVE_SPEED_MULT.value())));
        if (chance(random, MOB_SKELETON__USE_SWORD_CHANCE.value())) {
            Item weapon;
            if (chance(random, MOB_SKELETON__DIAMOND_SWORD_CHANCE.value())) {
                weapon = Items.DIAMOND_SWORD;
                setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.005f);
            } else {
                weapon = Items.IRON_SWORD;
            }
            setStackInHand(Hand.MAIN_HAND, new ItemStack(weapon));
        }
    }
}
