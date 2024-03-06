package me.xleiten.rebalance.core.mixins.world.entity.improvements.player;

import com.mojang.authlib.GameProfile;
import me.xleiten.rebalance.api.config.Option;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.xleiten.rebalance.Settings.PLAYER_SETTINGS;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity
{
    @Unique private static final Option<Boolean> INVULNERABLE_ON_SPAWN = PLAYER_SETTINGS.option("invulnerable-on-spawn", false);

    @Shadow private int joinInvulnerabilityTicks;

    protected MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile)
    {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    protected void removeJoinInvulnerability(CallbackInfo ci) {
        if (!INVULNERABLE_ON_SPAWN.getValue())
            joinInvulnerabilityTicks = 0;
    }
}
