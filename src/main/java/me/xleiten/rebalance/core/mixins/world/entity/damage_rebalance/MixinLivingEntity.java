package me.xleiten.rebalance.core.mixins.world.entity.damage_rebalance;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.config.Option;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static me.xleiten.rebalance.Settings.ARMOR_BUFFS;
import static me.xleiten.rebalance.Settings.DAMAGE_MULTIPLIERS;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity
{
    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Unique private static final Option<Float> LAVA_DAMAGE_MULT = DAMAGE_MULTIPLIERS.option("lava-damage-mult", 2.5f);
    @Unique private static final Option<Float> FIRE_DAMAGE_MULT = DAMAGE_MULTIPLIERS.option("fire-damage-mult", 1.75f);
    @Unique private static final Option<Float> IN_WALL_DAMAGE_MULT = DAMAGE_MULTIPLIERS.option("in-wall-damage-mult", 2.25f);
    @Unique private static final Option<Float> DROWN_DAMAGE_MULT = DAMAGE_MULTIPLIERS.option("drown-damage-mult", 2.0f);
    @Unique private static final Option<Float> NETHERITE_FIRE_RESISTANCE = ARMOR_BUFFS.option("netherite-fire-resistance-per-piece", 0.04f);
    @Unique private static final Option<Float> NETHERITE_LAVA_RESISTANCE = ARMOR_BUFFS.option("netherite-lava-resistance-per-piece", 0.03f);

    protected MixinLivingEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @ModifyVariable(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;isSleeping()Z"
            ),
            argsOnly = true,
            index = 2
    )
    public float changeDamageBySource(float value, @Local(argsOnly = true) DamageSource source) {
        var result = value;

        if (source.isOf(DamageTypes.IN_WALL)) {
            result *= DROWN_DAMAGE_MULT.getValue();
        } else if (source.isOf(DamageTypes.IN_FIRE)) {
            result *= IN_WALL_DAMAGE_MULT.getValue();
        } else if (source.isOf(DamageTypes.LAVA)) {
            result *= LAVA_DAMAGE_MULT.getValue() * (1 - getEquippedArmorPiecesAmount(ArmorMaterials.NETHERITE) * NETHERITE_LAVA_RESISTANCE.getValue());
        } else if (source.isOf(DamageTypes.IN_FIRE)) {
            result *= FIRE_DAMAGE_MULT.getValue() * (1 - getEquippedArmorPiecesAmount(ArmorMaterials.NETHERITE) * NETHERITE_FIRE_RESISTANCE.getValue());
        } else if (source.isOf(DamageTypes.DROWN)) {
            result *= DROWN_DAMAGE_MULT.getValue();
            if (getEquippedStack(EquipmentSlot.HEAD).isOf(Items.TURTLE_HELMET)) {
                result *= 0.80F;
            }
        }

        return result;
    }

    @Unique
    public int getEquippedArmorPiecesAmount(ArmorMaterials expectedMaterial) {
        var amount = 0;
        for (ItemStack stack: getArmorItems()) {
            if (stack.getItem() instanceof ArmorItem item && item.getMaterial() == expectedMaterial)
                amount++;
        }
        return amount;
    }
}
