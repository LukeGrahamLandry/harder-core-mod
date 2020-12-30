package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.entities.WonderingSpiritEntity;
import com.lukegraham.hardercore.init.EntityInit;
import com.lukegraham.hardercore.world_data.HarderCoreData;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BloodMoonHandler {
    private static final Random rand = new Random();
    private static ArrayList<LivingEntity> spawnedMobs = new ArrayList<>();

    public static boolean isBloodMoon(World world){
        int sleeps = HarderCoreData.getInstance(world).getSleeps();
        return sleeps % 8 == 0;
    }

    public static void spawnMonsters(PlayerEntity player) {
        World world = player.getEntityWorld();
        boolean isOverworld = player.getEntityWorld().getBiome(player.getPosition()).getCategory() != Biome.Category.NETHER && player.getEntityWorld().getBiome(player.getPosition()).getCategory() != Biome.Category.THEEND;
        if (!isOverworld) return;

        updateSleeps(world);
        if (isBloodMoon(world) && world.isNightTime()){
            MobEntity mob = (MobEntity) getNextMob().create(world);
            mob = trySummonNearPlayer(player, mob);
            if (mob != null) {
                mob.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(mob.getPosition()), SpawnReason.NATURAL, null, null);
                spawnedMobs.add(mob);
            }
        }
    }

    @SubscribeEvent
    public static void blockSleep(SleepingTimeCheckEvent event){
        LivingEntity player = event.getEntityLiving();
        if (isBloodMoon(player.getEntityWorld())){
            player.sendMessage(new StringTextComponent("You can't sleep during a blood moon."), player.getUniqueID());
            event.setResult(Event.Result.DENY);
        } else {
            event.setResult(Event.Result.DEFAULT);
        }
    }

    static boolean wasNightLastTime = false;
    private static void updateSleeps(World world){
        if (wasNightLastTime && !world.isNightTime()){
            if (isBloodMoon(world)){
                spawnedMobs.forEach((mob) -> {
                    if (mob.isAlive()) {
                        doParticles(mob, ParticleTypes.LARGE_SMOKE);
                        mob.remove();
                    }
                });
            }
            HarderCoreData.getInstance(world).increaseSleeps();
        }
        wasNightLastTime = world.isNightTime();
    }

    private static MobEntity trySummonNearPlayer(PlayerEntity player, MobEntity mob){
        for (int i=0;i<32;i++){
            Vector3d pos = randPos(player);
            boolean isValidPos = player.getEntityWorld().canSeeSky(new BlockPos(pos)) && mob.attemptTeleport(pos.x, pos.y, pos.z, true);
            if (isValidPos){
                player.getEntityWorld().addEntity(mob);
                doParticles(mob, ParticleTypes.LARGE_SMOKE);
                return mob;
            }
        }
        return null;
    }

    private static EntityType getNextMob(){
        int i = rand.nextInt(5);
        switch (i){
            case 0: return EntityType.ZOMBIE;
            case 1: return EntityType.SKELETON;
            case 2: return EntityType.SPIDER;
            case 3: return EntityType.CREEPER;
            case 4: {
                int j = rand.nextInt(4);
                switch (j){
                    case 0: return EntityType.WITCH;
                    case 1: return EntityType.ENDERMAN;
                    case 2:
                    case 3:
                        return EntityType.ZOMBIE;
                }
            }
        }

        return EntityType.ZOMBIE;
    }

    private static Vector3d randPos(PlayerEntity player){
        int range = 24;
        double x = player.getPosX() + (rand.nextDouble() - 0.5D) * range;
        double y = player.getPosY() + (double)(rand.nextInt(6) - 3);
        double z = player.getPosZ() + (rand.nextDouble() - 0.5D) * range;

        return new Vector3d(x, y, z);
    }

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
}
