package me.xleiten.rebalance.api.game.world.entity;

import net.minecraft.entity.data.TrackedData;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface SkinHolder
{
    @NotNull TrackedData<Byte> cringeMod$getModelPartsTracker();

    Optional<SkinData> cringeMod$getSkinData();

    void cringeMod$setSkinData(SkinData skinData, boolean reload);

    void cringeMod$updateSkin();
}
