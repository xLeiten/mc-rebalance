package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements;

import me.xleiten.rebalance.api.game.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.world.entity.mob.SpiderType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CaveSpiderEntity.class)
public abstract class MixinCaveSpiderEntity extends MixinSpiderEntity
{
    protected MixinCaveSpiderEntity(EntityType<? extends HostileEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void rebalanceMod$onFirstSpawn(ServerWorldAccess world, Random random, SpawnReason reason) {
        super.rebalanceMod$onFirstSpawn(world, random, reason);
        spiderType = SpiderType.CAVE;
    }

    @Override
    public boolean tryAttack(Entity entity) {
        return super.tryAttack(entity);
    }
}
