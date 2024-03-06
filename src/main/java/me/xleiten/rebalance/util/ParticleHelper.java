package me.xleiten.rebalance.util;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Range;

public class ParticleHelper
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

    public static void randomInBox(ServerWorld world, ParticleEffect particle, Box box, Vec3d offset, double speed, int count) {
        Random random = world.getRandom();
        double offsetX = box.getLengthX() * random.nextFloat();
        double offsetY = box.getLengthY() * random.nextFloat();
        double offsetZ = box.getLengthZ() * random.nextFloat();
        world.spawnParticles(ParticleTypes.ENCHANT, box.minX + offsetX, box.minY + offsetY, box.minZ + offsetZ, count, offset.x, offset.y, offset.z, speed);
    }

    public static void randomInBox(ServerWorld world, ParticleEffect particle, Box box, Vec3d velocity, double speed) {
        randomInBox(world, particle, box, velocity, speed, 0);
    }

    public static void randomInBox(ServerWorld world, ParticleEffect particle, Box box, double speed, int count) {
        randomInBox(world, particle, box, new Vec3d(0, 0, 0), speed, count);
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
