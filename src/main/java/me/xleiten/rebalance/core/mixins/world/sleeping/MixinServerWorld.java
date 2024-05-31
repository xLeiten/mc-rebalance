package me.xleiten.rebalance.core.mixins.world.sleeping;

import me.xleiten.rebalance.Settings;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.SleepManager;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld extends World
{
    @Shadow @Final private SleepManager sleepManager;

    @Shadow public abstract void setTimeOfDay(long timeOfDay);

    @Shadow
    @Final List<ServerPlayerEntity> players;

    @Shadow @Final private MinecraftServer server;

    private MixinServerWorld(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Redirect(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/SleepManager;canSkipNight(I)Z")
    )
    public boolean canSkipNight(SleepManager instance, int percentage) {
        return false;
    }

    @Inject(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;calculateAmbientDarkness()V")
    )
    public void canNightBeSkipped(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (sleepManager.canSkipNight(0) && sleepManager.canResetTime(0, this.players)) {
            if (getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
                var multiplier = Settings.CUSTOM_SLEEPING__MAX_NIGHT_SKIP_MULTIPLIER.value() * (sleepManager.getSleeping() / this.players.size());
                this.setTimeOfDay(this.getTimeOfDay() + multiplier);
                server.getPlayerManager().sendToAll(new WorldTimeUpdateS2CPacket(getTime(), getTimeOfDay(), getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)));
            }
        }
    }
}
