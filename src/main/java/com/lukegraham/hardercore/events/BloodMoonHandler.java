package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.config.HarderCoreConfig;
import com.lukegraham.hardercore.util.Helper;
import com.lukegraham.hardercore.world_data.HarderCoreData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
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
        int interval = HarderCoreConfig.bloodmoonInterval.get();
        if (interval == 0) return true;
        if (interval < 0) return false;

        int sleeps = HarderCoreData.getInstance(world).getSleeps();
        return (sleeps + 1) % interval == 0;
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
                        Helper.doParticles(mob, ParticleTypes.LARGE_SMOKE);
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
            boolean canSeeSky = player.getEntityWorld().canSeeSky(new BlockPos(pos));
            boolean isDark = player.getEntityWorld().getLightFor(LightType.BLOCK, new BlockPos(pos)) < 2;
            boolean isValidPos = canSeeSky && isDark && mob.attemptTeleport(pos.x, pos.y, pos.z, true);
            HarderCore.LOGGER.debug(isDark);
            if (isValidPos){
                player.getEntityWorld().addEntity(mob);
                Helper.doParticles(mob, ParticleTypes.LARGE_SMOKE);
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
}
