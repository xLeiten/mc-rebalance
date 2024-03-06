package me.xleiten.rebalance.core.mixins.event.world.entity.mob.nbt;

import me.xleiten.rebalance.api.game.world.NbtHolder;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MixinMobEntity implements NbtHolder
{
    @Inject(
            method = "writeCustomDataToNbt",
            at = @At("TAIL")
    )
    public void addCustomNbtWriteHook(NbtCompound nbt, CallbackInfo ci) {
        cringeMod$onCustomNbtDataWrite(nbt);
    }

    @Inject(
            method = "readCustomDataFromNbt",
            at = @At("TAIL")
    )
    public void addCustomNbtReadHook(NbtCompound nbt, CallbackInfo ci) {
        cringeMod$onCustomNbtDataRead(nbt);
    }
}
