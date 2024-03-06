package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.ai.brain.task.VillagerBreedTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(VillagerBreedTask.class)
public abstract class MixinVillagerBreedTask {

    @ModifyArg(
            method = "goHome",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ai/brain/task/VillagerBreedTask;setChildHome(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;Lnet/minecraft/util/math/BlockPos;)V"
            ),
            index = 1
    )
    public VillagerEntity onVillagerBreed(VillagerEntity child) {
        var world = (ServerWorld) child.getWorld();
        ((InitializableMobEntity) child).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.BREEDING, child.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(child, world, SpawnReason.BREEDING, child.getPos());
        return child;
    }

}
