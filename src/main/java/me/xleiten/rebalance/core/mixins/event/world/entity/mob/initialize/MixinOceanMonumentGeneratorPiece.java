package me.xleiten.rebalance.core.mixins.event.world.entity.mob.initialize;

import com.llamalad7.mixinextras.sugar.Local;
import me.xleiten.rebalance.api.game.event.world.entity.mob.MobEntityEvents;
import me.xleiten.rebalance.api.game.event.world.entity.mob.SpawnReason;
import me.xleiten.rebalance.api.game.event.world.entity.mob.InitializableMobEntity;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.StructureWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/structure/OceanMonumentGenerator$Piece")
public abstract class MixinOceanMonumentGeneratorPiece {

    @Inject(
            method = "spawnElderGuardian",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ElderGuardianEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/EntityData;",
                    shift = At.Shift.AFTER
            )
    )
    public void onElderGuardianGenerate(StructureWorldAccess world, BlockBox box, int x, int y, int z, CallbackInfo ci, @Local ElderGuardianEntity elderGuardianEntity) {
        ((InitializableMobEntity) elderGuardianEntity).cringeMod$onMobInitialize(world, world.getRandom(), SpawnReason.STRUCTURE, elderGuardianEntity.getPos(), world.getDifficulty());
        MobEntityEvents.INITIALIZE.invoker().initialize(elderGuardianEntity, world, SpawnReason.STRUCTURE, elderGuardianEntity.getPos());
    }

}
