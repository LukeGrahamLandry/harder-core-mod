package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.entities.WonderingSpiritEntity;
import com.lukegraham.hardercore.init.EntityInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;
import java.util.Random;

public class BloodMoonHandler {
    private static final Random rand = new Random();
    public static void spawnSpirits(PlayerEntity player) {
        World world = player.getEntityWorld();
        long time = (long) Math.floor(world.getGameTime() / 24000.0D);
        HarderCore.LOGGER.debug(time);
        boolean isBloodMoon = time % 8 == 0;
        if (isBloodMoon && world.isNightTime()){
            WonderingSpiritEntity mob = new WonderingSpiritEntity(EntityInit.WONDERING_SPIRIT.get(), world);
            for (int i=0;i<16;i++){
                Vector3d pos = randPos(player);
                boolean flag = world.canSeeSky(new BlockPos(pos)) && mob.attemptTeleport(pos.x, pos.y, pos.z, true);
                if (flag){
                    world.addEntity(mob);
                    mob.doParticles(ParticleTypes.LARGE_SMOKE);
                    break;
                }
            }


        }
    }

    private static Vector3d randPos(PlayerEntity player){
        int range = 24;
        double x = player.getPosX() + (rand.nextDouble() - 0.5D) * range;
        double y = player.getPosY() + (double)(rand.nextInt(6) - 3);
        double z = player.getPosZ() + (rand.nextDouble() - 0.5D) * range;

        return new Vector3d(x, y, z);
    }
}
