package com.lukegraham.hardercore.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class Helper {
    static Random rand = new Random();

    public static void doParticles(LivingEntity mob, IParticleData t){
        if (!mob.getEntityWorld().isRemote()) {
            for (int i = 0; i < 20; i++) {
                AxisAlignedBB box = mob.getBoundingBox();
                Vector3d pos = box.getCenter().add(randomize(box.getXSize()), randomize(box.getYSize()), randomize(box.getZSize()));
                ((ServerWorld) mob.getEntityWorld()).spawnParticle(t, pos.x, pos.y, pos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }
    private static double randomize(double d){
        int factor = rand.nextInt(2) == 0 ? 1 : -1;
        return rand.nextFloat() * (d / 2) * factor;
    }

    public static int randInt(int bound){
        return rand.nextInt(bound);
    }

    public static List<Entity> getNearbyEntities(World world, BlockPos pos, int range){
        return world.getEntitiesWithinAABBExcludingEntity(null, getAABB(pos, range));
    }

    public static AxisAlignedBB getAABB(BlockPos pos, int range) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        return new AxisAlignedBB(x-range,y-range,z-range,x+range,y+range,z+range);
    }
}
