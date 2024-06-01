package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import me.xleiten.rebalance.Settings;
import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.util.AttributeHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShulkerEntity.class)
public abstract class MixinShulkerEntity extends MixinMobEntity
{
    protected MixinShulkerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason) {
        super.rebalanceMod$onFirstSpawn(world, random, reason);
        AttributeHelper.setBaseValue(this, EntityAttributes.GENERIC_MAX_HEALTH, Settings.MOB_SHULKER__MAX_HEALTH.value());
    }
}
