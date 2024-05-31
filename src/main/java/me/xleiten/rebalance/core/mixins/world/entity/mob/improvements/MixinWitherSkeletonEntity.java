package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.util.ItemHelper;
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
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import static me.xleiten.rebalance.Settings.*;
import static me.xleiten.rebalance.api.math.RandomHelper.chance;
import static me.xleiten.rebalance.api.math.RandomHelper.range;
import static me.xleiten.rebalance.util.AttributeHelper.addModifier;
import static me.xleiten.rebalance.util.AttributeHelper.modifier;

@Mixin(WitherSkeletonEntity.class)
public abstract class MixinWitherSkeletonEntity extends MixinHostileEntity
{
    protected MixinWitherSkeletonEntity(EntityType<? extends HostileEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason) {
        super.rebalanceMod$onFirstSpawn(world, random, reason);
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", range(random, MOB_WITCH__MOVE_SPEED_MULT.value())));
        var stackInHand = getStackInHand(Hand.MAIN_HAND);
        if (stackInHand.getItem() instanceof SwordItem) {
            if (chance(random, MOB_WITHER_SKELETON__SWORD_SHARPNESS_CHANCE.value())) stackInHand.addEnchantment(Enchantments.SHARPNESS, range(random, MOB_WITHER_SKELETON__SWORD_SHARPNESS_LEVEL.value()));
            if (chance(random, MOB_WITHER_SKELETON__SWORD_FIREASPECT_CHANCE.value())) stackInHand.addEnchantment(Enchantments.FIRE_ASPECT, range(random, MOB_WITHER_SKELETON__SWORD_FIREASPECT_LEVEL.value()));
        }
    }

    @Override
    public void initEquipment(Random random, LocalDifficulty localDifficulty) {
        if (chance(random, MOB_WITHER_SKELETON__NETHERITE_EQUIPMENT_CHANCE.value())) {
            equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_SWORD));
            equipStack(EquipmentSlot.CHEST, ItemHelper.applyTrim(getWorld().getServer().getRegistryManager(), ArmorTrimMaterials.REDSTONE, ArmorTrimPatterns.RIB, new ItemStack(Items.NETHERITE_CHESTPLATE)));
            setEquipmentDropChance(EquipmentSlot.CHEST, 0.001f);
            setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.001f);
        } else
            equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
    }
}
