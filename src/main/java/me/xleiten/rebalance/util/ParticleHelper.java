package me.xleiten.rebalance.util;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Range;

public final class ParticleHelper
{
    public static final double RADIAN = Math.PI / 180;

    public static void spawn(ServerWorld world, ParticleEffect particle, Vec3d pos, Vec3d delta, double speed, int count) {
        world.spawnParticles(particle, pos.x, pos.y, pos.z, count, delta.x, delta.y, delta.z, speed);
    }

    public static void spawn(ServerWorld world, ParticleEffect particle, Vec3d pos, double deltaX, double deltaY, double deltaZ, double speed, int count) {
        world.spawnParticles(particle, pos.x, pos.y, pos.z, count, deltaX, deltaY, deltaZ, speed);
    }

    public static void spawn(ServerWorld world, ParticleEffect particle, double x, double y, double z, Vec3d delta, double speed, int count) {
        world.spawnParticles(particle, x, y, z, count, delta.x, delta.y, delta.z, speed);
    }

    public static void spawnInLine(ServerWorld world, ParticleEffect particle, Vec3d start, Vec3d end, int count) {
        double deltaX = (end.x - start.x) / count;
        double deltaZ = (end.z - start.z) / count;
        double deltaY = (end.y - start.y) / count;
        Vec3d pos = start;
        for (int i = 0; i < count; i++) {
            world.spawnParticles(particle, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
            pos = pos.add(deltaX, deltaY, deltaZ);
        }
    }

    public static void spawnCircular(ServerWorld world, ParticleEffect particle, double radius, Vec3d pos, Vec3d delta, double speed, int count, @Range(from = 0, to = 360) int step) {
        for (int i = 0; i < 360; i += step) {
            double radians = i * RADIAN;
            spawn(world, particle, pos.x + (Math.sin(radians) * radius), pos.y, pos.z + (Math.cos(radians) * radius), delta, speed, count);
        }
    }
}
