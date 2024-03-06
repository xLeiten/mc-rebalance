package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.structure.WoodlandMansionGenerator;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WoodlandMansionGenerator.Piece.class)
public abstract class MixinWoodlandMansionGeneratorPiece {

    @Inject(
            method = "handleMetadata",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/ServerWorldAccess;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
            )
    )
    public void onPillagerGenerate(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox, CallbackInfo ci, @Local MobEntity mobEntity) {
        ((InitializableMobEntity) mobEntity).cringeMod$onMobInitialize(world, random, SpawnReason.STRUCTURE, mobEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(mobEntity, world, SpawnReason.STRUCTURE, mobEntity.getPos());
    }

}
