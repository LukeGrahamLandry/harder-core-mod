package com.lukegraham.hardercore.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HarshEnvironmentTickHandler {
    private static final Random rand = new Random();

    @SubscribeEvent
    public static void checkAndApplyThirstRandomly(TickEvent.PlayerTickEvent event){
        boolean validPlayer = !event.player.getEntityWorld().isRemote() && !(event.player.isSpectator() || event.player.isCreative());
        if (validPlayer && rand.nextInt(200) == 0){
            PlayerEntity player = event.player;

            ShadowKillerSpawnHandler.spawnShadowKillersInDarkness(player);
            HungryHandler.checkAndApplyHungry(player);
            if (rand.nextInt(50) == 0) ExhaustionHandler.applyExhaustion(player);
            if (rand.nextInt(4) == 0) TempuratureHandler.updatePlayerTemp(player);
            if (rand.nextInt(1) == 0) BloodMoonHandler.spawnMonsters(player);
            AirQualityHandler.increaseAirQualityOverTime(player);
            ThirstHandler.checkAndApplyThirst(player);
        }
    }
}
