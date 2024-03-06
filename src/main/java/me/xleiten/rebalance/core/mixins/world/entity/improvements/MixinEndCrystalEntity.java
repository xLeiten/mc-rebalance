package me.xleiten.rebalance.core.mixins.world.entity.improvements;

import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.event.world.entity.SpawnableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static me.xleiten.rebalance.Settings.END_CRYSTAL_SETTINGS;

@Mixin(EndCrystalEntity.class)
public abstract class MixinEndCrystalEntity extends Entity implements SpawnableEntity
{
    @Unique private static final Option<Boolean> EXPLODE_WITH_FIRE = END_CRYSTAL_SETTINGS.option("explode-with-fire", true);

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
        return EXPLODE_WITH_FIRE.getValue();
    }
}
