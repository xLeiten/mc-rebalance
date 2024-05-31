package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.math.RandomHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import static me.xleiten.rebalance.Settings.MOB_WITCH__MOVE_SPEED_MULT;
import static me.xleiten.rebalance.util.AttributeHelper.addModifier;
import static me.xleiten.rebalance.util.AttributeHelper.modifier;

@Mixin(WitchEntity.class)
public abstract class MixinWitchEntity extends MixinHostileEntity
{
    protected MixinWitchEntity(EntityType<? extends HostileEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason) {
        super.rebalanceMod$onFirstSpawn(world, random, reason);
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", RandomHelper.range(random, MOB_WITCH__MOVE_SPEED_MULT.value())));
    }
}
