package me.xleiten.rebalance.core.mixins.event.world.entity;

import me.xleiten.rebalance.api.game.event.world.entity.SpawnableEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class MixinEntity implements SpawnableEntity
{

}
