package me.xleiten.rebalance.core.mixins.world.chunk;

import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.PlayerAssociatedNetworkHandler;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ThreadedAnvilChunkStorage.EntityTracker.class)
public interface EntityTrackerAccessor
{
    @Accessor
    EntityTrackerEntry getEntry();

    @Accessor
    Set<PlayerAssociatedNetworkHandler> getListeners();
}
