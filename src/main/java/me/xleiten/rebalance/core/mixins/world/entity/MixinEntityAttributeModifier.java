package me.xleiten.rebalance.core.mixins.world.entity;

import me.xleiten.rebalance.api.game.world.entity.mob.attribute.AttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityAttributeModifier.class)
public abstract class MixinEntityAttributeModifier implements AttributeModifier
{
    @Shadow @Final private String name;
    @Mutable @Shadow @Final private double value;

    @Override
    public void rebalanceMod$setValue(double value) {
        this.value = value;
    }
}
