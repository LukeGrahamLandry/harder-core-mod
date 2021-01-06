package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.HarderCore;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;


// !!! STUFF DOESNT HAPPEN WHEN YOU'RE IN CREATIVE MODE !!!
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HarshEnvironmentTickHandler {
    private static final Random rand = new Random();

    @SubscribeEvent
    public static void updateHarshEnvironmentRandomly(TickEvent.PlayerTickEvent event){
        boolean validPlayer = !event.player.getEntityWorld().isRemote() && !(event.player.isSpectator() || event.player.isCreative());
        if (validPlayer && rand.nextInt(200) == 0){
            PlayerEntity player = event.player;

            ShadowKillerSpawnHandler.spawnShadowKillersInDarkness(player);
            HungryHandler.checkAndApplyHungry(player);
            if (rand.nextInt(50) == 0) ExhaustionHandler.applyExhaustion(player);
            if (rand.nextInt(3) == 0) TempuratureHandler.updatePlayerTemp(player);
            BloodMoonHandler.spawnMonsters(player);
            if (rand.nextInt(2) == 0) AirQualityHandler.increaseAirQualityOverTime(player);
            ThirstHandler.checkAndApplyThirst(player);
        }
    }
}
