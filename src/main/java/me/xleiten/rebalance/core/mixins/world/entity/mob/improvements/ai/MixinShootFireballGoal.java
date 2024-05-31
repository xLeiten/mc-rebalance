package me.xleiten.rebalance.core.mixins.world.entity.mob.improvements.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net/minecraft/entity/mob/GhastEntity$ShootFireballGoal")
public abstract class MixinShootFireballGoal extends Goal
{
    @Shadow @Final private GhastEntity ghast;

    @Shadow public int cooldown;

    /**
     * @author xLeiten
     * @reason cant do another
     */
    @Overwrite
    public void tick() {
        LivingEntity livingEntity = this.ghast.getTarget();
        if (livingEntity != null) {
            if (livingEntity.squaredDistanceTo(this.ghast) < 4096.0 && this.ghast.canSee(livingEntity)) {
                World world = this.ghast.getWorld();
                ++this.cooldown;
                if (this.cooldown == 10 && !this.ghast.isSilent()) {
                    world.syncWorldEvent(null, WorldEvents.GHAST_WARNS, this.ghast.getBlockPos(), 0);
                }

                if (this.cooldown == 20 || this.cooldown == 40) {
                    Vec3d vec3d = this.ghast.getRotationVec(1.0F);
                    double f = livingEntity.getX() - (this.ghast.getX() + vec3d.x * 4.0);
                    double g = livingEntity.getBodyY(0.5) - (0.5 + this.ghast.getBodyY(0.5));
                    double h = livingEntity.getZ() - (this.ghast.getZ() + vec3d.z * 4.0);
                    if (!this.ghast.isSilent()) {
                        world.syncWorldEvent(null, WorldEvents.GHAST_SHOOTS, this.ghast.getBlockPos(), 0);
                    }

                    FireballEntity fireballEntity = new FireballEntity(world, this.ghast, f, g, h, this.ghast.getFireballStrength());
                    fireballEntity.setPosition(this.ghast.getX() + vec3d.x * 4.0, this.ghast.getBodyY(0.5) + 0.5, fireballEntity.getZ() + vec3d.z * 4.0);
                    world.spawnEntity(fireballEntity);
                }

                if (this.cooldown >= 40) {
                    this.cooldown = -40;
                }
            } else if (this.cooldown > 0) {
                --this.cooldown;
            }

            this.ghast.setShooting(this.cooldown > 10);
        }
    }


}
