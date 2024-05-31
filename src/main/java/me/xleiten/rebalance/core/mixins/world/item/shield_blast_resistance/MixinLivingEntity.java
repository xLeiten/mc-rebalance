package me.xleiten.rebalance.core.mixins.world.item.shield_blast_resistance;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.Settings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity
{
    @Shadow public abstract Hand getActiveHand();
    @Shadow protected ItemStack activeItemStack;

    @Shadow public abstract void equipStack(EquipmentSlot slot, ItemStack stack);

    @Shadow
    public static EquipmentSlot getSlotForHand(Hand hand) {
        return null;
    }

    protected MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V",
                    shift = At.Shift.BEFORE
            ),
            argsOnly = true,
            index = 2
    )
    protected float modifyDamageByBlastResistance(float value, @Local(argsOnly = true) DamageSource source) {
        if (source.isIn(DamageTypeTags.IS_EXPLOSION)) {
            var level = EnchantmentHelper.getLevel(Enchantments.BLAST_PROTECTION, activeItemStack);
            return level > 0 ? (value * (1 - (level * Settings.ITEM_SHIELD__BLAST_RESISTANCE_PER_LEVEL.value()))) : value;
        }
        return value;
    }

    @ModifyVariable(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z",
                    ordinal = 1
            ),
            argsOnly = true,
            index = 2
    )
    protected float tryAbsorbExplosionDamage(float value, @Local(ordinal = 2) float g, @Local(argsOnly = true) DamageSource source) {
        if (source.isIn(DamageTypeTags.IS_EXPLOSION)) {
            return (1 - Settings.ITEM_SHIELD__EXPLOSION_ABSORPTION.value()) * g;
        }
        return value;
    }

    @Inject(
            method = "damageShield",
            at = @At("HEAD")
    )
    protected void addShieldDamageLogic(float amount, CallbackInfo ci) {
        if (this.activeItemStack.isOf(Items.SHIELD)) {
            if (amount >= ShieldItem.MIN_DAMAGE_AMOUNT_TO_BREAK) {
                int i = 1 + MathHelper.floor(amount);
                Hand hand = this.getActiveHand();
                this.activeItemStack.damage(i, (LivingEntity) (Object) this, getSlotForHand(hand));
                if (this.activeItemStack.isEmpty()) {
                    this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.getWorld().random.nextFloat() * 0.4F);
                    if (hand == Hand.MAIN_HAND) {
                        this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    } else {
                        this.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    }
                }
            }
        }
    }
}
