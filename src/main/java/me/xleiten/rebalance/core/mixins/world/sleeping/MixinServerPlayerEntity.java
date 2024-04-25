package me.xleiten.rebalance.core.mixins.world.sleeping;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.util.TimeUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity
{
    @Unique private static final Option<Integer> TICKS_DELTA = Settings.CUSTOM_SLEEPING.option("formatted-time-ticks-delta", 7000);
    @Shadow public abstract ServerWorld getServerWorld();

    @Shadow public ServerPlayNetworkHandler networkHandler;

    private MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile)
    {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "trySleep", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;updateSleepingPlayers()V"))
    public void onPlayerStartSleep(BlockPos pos, CallbackInfoReturnable<Either<SleepFailureReason, Unit>> cir) {
        this.networkHandler.sendPacket(new TitleFadeS2CPacket(0, 20, 10));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onPlayerSleeping(CallbackInfo ci) {
        if (getSleepTimer() >= 100) {
            this.networkHandler.sendPacket(new TitleS2CPacket(Text.of(TimeUtils.format(getServerWorld(), TICKS_DELTA.getValue()))));
        }
    }
}
