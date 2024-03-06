package me.xleiten.rebalance.core.mixins.world.entity;

import me.xleiten.rebalance.api.game.world.entity.ArmorStand;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ArmorStandEntity.class)
public abstract class MixinArmorStand implements ArmorStand
{
    @Shadow protected abstract void setSmall(boolean small);

    @Shadow protected abstract void setMarker(boolean marker);

    @Override
    public void cringeMod$setIsSmall(boolean isSmall) {
        setSmall(isSmall);
    }

    @Override
    public void cringeMod$setIsMarker(boolean marker) {
        setMarker(marker);
    }
}
