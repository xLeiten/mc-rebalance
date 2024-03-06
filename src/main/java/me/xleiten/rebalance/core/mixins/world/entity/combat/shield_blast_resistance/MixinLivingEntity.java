package me.xleiten.rebalance.core.mixins.world.entity.combat.shield_blast_resistance;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.config.Option;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.xleiten.rebalance.Settings.DAMAGE_MULTIPLIERS;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity
{
    @Unique private static final Option<Float> SHIELD_EXPLOSION_ABSORPTION = DAMAGE_MULTIPLIERS.option("explosion-shield-absorption", 0.90f);
    @Unique private static final Option<Float> SHIELD_BLAST_RESISTANCE_MULT_PER_LEVEL = DAMAGE_MULTIPLIERS.option("shield-blast-resistance-mult-per-level", 0.15f);

    @Shadow public abstract Hand getActiveHand();
    @Shadow protected ItemStack activeItemStack;

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
    protected float modifyDamageByBlastResistance(float value, @Local DamageSource source) {
        if (source.isIn(DamageTypeTags.IS_EXPLOSION)) {
            var level = EnchantmentHelper.getLevel(Enchantments.BLAST_PROTECTION, activeItemStack);
            return level > 0 ? (value * (1 - (level * SHIELD_BLAST_RESISTANCE_MULT_PER_LEVEL.getValue()))) : value;
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
    protected float tryAbsorbExplosionDamage(float value, @Local(ordinal = 2) float g, @Local DamageSource source) {
        if (source.isIn(DamageTypeTags.IS_EXPLOSION)) {
            return (1 - SHIELD_EXPLOSION_ABSORPTION.getValue()) * g;
        }
        return value;
    }

    @Inject(
            method = "damageShield",
            at = @At("HEAD")
    )
    protected void writeShieldDamageLogic(float amount, CallbackInfo ci) {
        if (this.activeItemStack.isOf(Items.SHIELD)) {
            if (amount >= ShieldItem.MIN_DAMAGE_AMOUNT_TO_BREAK) {
                int i = 1 + MathHelper.floor(amount);
                Hand hand = this.getActiveHand();
                this.activeItemStack.damage(i, (LivingEntity) (Object) this, entity -> {});
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
