package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs;

import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.util.math.DoubleRange;
import me.xleiten.rebalance.util.math.RandomHelper;
import me.xleiten.rebalance.util.math.Range;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import static me.xleiten.rebalance.Settings.WITCH_SETTINGS;
import static me.xleiten.rebalance.util.AttributeHelper.*;

@Mixin(WitchEntity.class)
public abstract class MixinWitchEntity extends MixinHostileEntity
{
    @Unique private static final Option<DoubleRange> MOVE_SPEED_MULT = WITCH_SETTINGS.option("move-speed-mult", Range.create(0.1, 0.2));

    protected MixinWitchEntity(EntityType<? extends HostileEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void cringeMod$onMobInitialize(ServerWorldAccess world, Random random, SpawnReason reason, Vec3d pos, Difficulty difficulty) {
        super.cringeMod$onMobInitialize(world, random, reason, pos, difficulty);
        addModifier(this, EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier("moveSpeedBoost", RandomHelper.range(random, MOVE_SPEED_MULT.getValue())));
    }
}
