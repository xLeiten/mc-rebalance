package me.xleiten.rebalance.core.mixins.world.entity;

import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.game.event.world.entity.SpawnableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EndCrystalEntity.class)
public abstract class MixinEndCrystalEntity extends Entity implements SpawnableEntity
{
    private MixinEndCrystalEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyArg(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;"
            ),
            index = 7
    )
    public boolean explodeWithFire(boolean createFire) {
        return Settings.ENTITY_ENDCRYSTAL__EXPLODE_WITH_FIRE.value();
    }
}
