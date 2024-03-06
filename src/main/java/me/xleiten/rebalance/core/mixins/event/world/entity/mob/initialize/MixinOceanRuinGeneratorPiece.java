package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.structure.OceanRuinGenerator;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OceanRuinGenerator.Piece.class)
public abstract class MixinOceanRuinGeneratorPiece {

    @Inject(
            method = "handleMetadata",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/DrownedEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/EntityData;",
                    shift = At.Shift.AFTER
            )
    )
    public void onDrownedGenerate(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox, CallbackInfo ci, @Local DrownedEntity drownedEntity) {
        ((InitializableMobEntity) drownedEntity).cringeMod$onMobInitialize(world, random, SpawnReason.STRUCTURE, drownedEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(drownedEntity, world, SpawnReason.STRUCTURE, drownedEntity.getPos());
    }

}
