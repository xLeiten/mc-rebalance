package me.xleiten.rebalance.core.mixins.world.entity;

import me.xleiten.rebalance.api.game.world.entity.mob.attribute.AttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

@Mixin(EntityAttributeModifier.class)
public abstract class MixinEntityAttributeModifier implements AttributeModifier
{
    @Shadow @Final private String name;
    @Mutable @Shadow @Final private double value;

    @Override
    public @NotNull String cringeMod$getName() {
        return name;
    }

    @Override
    public void cringeMod$setValue(double value) {
        this.value = value;
    }
}
