package me.xleiten.rebalance.core.mixins.world.entity.improvements.mobs.ai;

import me.xleiten.rebalance.api.config.Option;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CreeperIgniteGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.xleiten.rebalance.Settings.CREEPER_SETTINGS;

@Mixin(CreeperIgniteGoal.class)
public abstract class MixinCreeperIgniteGoal
{
  @Unique
  private static final Option<Boolean> MOVE_WHILE_FUSE = CREEPER_SETTINGS.option("move-while-fuse", true);

  @Shadow @Nullable private LivingEntity target;

  @Shadow @Final private net.minecraft.entity.mob.CreeperEntity creeper;

  @Redirect(method = "start", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/pathing/EntityNavigation;stop()V"))
  public void redirect(EntityNavigation instance) {

  }

  @Inject(method = "tick", at = @At("TAIL"))
  public void tick(CallbackInfo ci) {
    if (MOVE_WHILE_FUSE.getValue() && target != null)
      creeper.getNavigation().startMovingTo(target, 1.05);
  }
}
