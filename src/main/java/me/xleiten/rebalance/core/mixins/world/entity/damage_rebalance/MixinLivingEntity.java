package me.xleiten.rebalance.core.mixins.world.entity.damage_rebalance;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.config.Option;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static me.xleiten.rebalance.Settings.DAMAGE_MULTIPLIERS;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity
{
    @Unique private static final Option<Float> LAVA_DAMAGE_MULT = DAMAGE_MULTIPLIERS.option("lava-damage-mult", 2.5f);
    @Unique private static final Option<Float> FIRE_DAMAGE_MULT = DAMAGE_MULTIPLIERS.option("fire-damage-mult", 1.75f);
    @Unique private static final Option<Float> IN_WALL_DAMAGE_MULT = DAMAGE_MULTIPLIERS.option("in-wall-damage-mult", 2.25f);
    @Unique private static final Option<Float> DROWN_DAMAGE_MULT = DAMAGE_MULTIPLIERS.option("drown-damage-mult", 2.0f);

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
    public float changeDamageBySource(float value, @Local DamageSource source) {
        if (source.isOf(DamageTypes.LAVA)) {
            return value * LAVA_DAMAGE_MULT.getValue();
        } else if (source.isOf(DamageTypes.DROWN)) {
            return value * DROWN_DAMAGE_MULT.getValue();
        } else if (source.isOf(DamageTypes.IN_FIRE)) {
            return value * FIRE_DAMAGE_MULT.getValue();
        } else if (source.isOf(DamageTypes.IN_WALL)) {
            return value * IN_WALL_DAMAGE_MULT.getValue();
        }
        return value;
    }

}
