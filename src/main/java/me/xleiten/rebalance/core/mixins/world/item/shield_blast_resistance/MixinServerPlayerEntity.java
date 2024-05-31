package me.xleiten.rebalance.core.mixins.world.item.shield_blast_resistance;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import me.xleiten.rebalance.Settings;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity
{
    @Shadow public abstract void sendMessage(Text message);

    protected MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile)
    {
        super(world, pos, yaw, gameProfile);
    }

    @WrapOperation(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    protected boolean onExplosionShieldBlock(ServerPlayerEntity instance, DamageSource source, float amount, Operation<Boolean> original) {
        var hasBlocked = isBlocking();
        var result = original.call(instance, source, amount);
        if (source.isOf(DamageTypes.EXPLOSION) || source.isOf(DamageTypes.PLAYER_EXPLOSION)) {
            if (isBlocking()) {
                var range = Settings.ITEM_SHIELD__ON_EXPLOSION_COOLDOWN.value();
                this.getItemCooldownManager().set(Items.SHIELD, MathHelper.clamp((int) amount, range.min, range.max));
                this.clearActiveItem();
                this.getWorld().sendEntityStatus(this, EntityStatuses.BREAK_SHIELD);
            } else {
                if (hasBlocked)
                    this.getItemCooldownManager().set(Items.SHIELD, 20);
            }
        }
        return result;
    }
}
