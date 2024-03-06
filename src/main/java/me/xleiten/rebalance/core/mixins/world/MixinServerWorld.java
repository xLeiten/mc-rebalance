package me.xleiten.rebalance.core.mixins.world;

import me.xleiten.rebalance.api.game.world.ServerLevel;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld implements ServerLevel
{
    @Shadow @Final private ServerEntityManager<Entity> entityManager;

    @Override
    public ServerEntityManager<Entity> cringeMod$getEntityManager() {
        return entityManager;
    }
}
