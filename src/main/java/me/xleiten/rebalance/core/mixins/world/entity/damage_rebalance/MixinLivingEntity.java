package me.xleiten.rebalance.core.mixins.world.entity.damage_rebalance;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.Settings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Function;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity
{
    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

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
            result *= Settings.DAMAGE_SOURCE__IN_WALL_MULT.value();
        } else if (source.isOf(DamageTypes.LAVA)) {
            var armorPieces = getEquippedArmorPiecesAmount(it -> it == ArmorMaterials.NETHERITE || it == ArmorMaterials.DIAMOND);
            result *= Settings.DAMAGE_SOURCE__LAVA_MULT.value() * (1 -
                    (armorPieces * Settings.ITEM_ARMOR__NETHERITE_FIRE_RESISTANCE.value()
                            + armorPieces * Settings.ITEM_ARMOR__DIAMOND_FIRE_RESISTANCE.value()));
        } else if (source.isOf(DamageTypes.IN_FIRE)) {
            var armorPieces = getEquippedArmorPiecesAmount(it -> it == ArmorMaterials.NETHERITE || it == ArmorMaterials.DIAMOND);
            result *= Settings.DAMAGE_SOURCE__FIRE_MULT.value() * (1 -
                    (armorPieces * Settings.ITEM_ARMOR__NETHERITE_FIRE_RESISTANCE.value()
                            + armorPieces * Settings.ITEM_ARMOR__DIAMOND_FIRE_RESISTANCE.value()));
        } else if (source.isOf(DamageTypes.DROWN)) {
            result *= Settings.DAMAGE_SOURCE__DROWN_MULT.value();
            if (getEquippedStack(EquipmentSlot.HEAD).isOf(Items.TURTLE_HELMET)) {
                result *= 0.80F;
            }
        }
        return result;
    }

    @Unique
    public int getEquippedArmorPiecesAmount(@NotNull Function<RegistryEntry<ArmorMaterial>, Boolean> filter) {
        var amount = 0;
        for (ItemStack stack: getArmorItems()) {
            if (stack.getItem() instanceof ArmorItem item && filter.apply(item.getMaterial()))
                amount++;
        }
        return amount;
    }
}
