package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.config.HarderCoreConfig;
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

            if (HarderCoreConfig.doDeadlyShadows.get())
                ShadowKillerSpawnHandler.spawnShadowKillersInDarkness(player);

            if (HarderCoreConfig.doHungerPains.get())
                HungryHandler.checkAndApplyHungry(player);


            if (HarderCoreConfig.doExhaustion.get() && rand.nextInt(50) == 0)
                ExhaustionHandler.applyExhaustion(player);

            if (HarderCoreConfig.doTemperature.get() && rand.nextInt(3) == 0)
                TempuratureHandler.updatePlayerTemp(player);

            BloodMoonHandler.spawnMonsters(player);

            if (HarderCoreConfig.doAirQuality.get() && rand.nextInt(2) == 0)
                AirQualityHandler.increaseAirQualityOverTime(player);

            if (HarderCoreConfig.doThirst.get())
                ThirstHandler.checkAndApplyThirst(player);
        }
    }
}
