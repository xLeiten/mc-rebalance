package me.xleiten.rebalance.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;

public class RaycastHelper
{
    @Nullable
    public static BlockPos raycast(LivingEntity caster, Vec3d start, Vec3d end, double distanceToBlockCenter) {
        var result = caster.getWorld().raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, caster));
        if (result != null) {
            var pos = result.getBlockPos();
            return start.squaredDistanceTo(pos.toCenterPos()) <= distanceToBlockCenter * distanceToBlockCenter ? pos : null;
        }
        return null;
    }
}
