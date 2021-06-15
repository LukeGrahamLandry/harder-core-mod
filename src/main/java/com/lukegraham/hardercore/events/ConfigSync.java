package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.config.ConfigPacket;
import com.lukegraham.hardercore.config.HarderCoreConfig;
import com.lukegraham.hardercore.util.PacketHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ConfigSync {
    @SubscribeEvent
    public static void syncOnLogin(PlayerEvent.PlayerLoggedInEvent event){
        if (!event.getPlayer().getEntityWorld().isRemote()){
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
                    new ConfigPacket(HarderCoreConfig.doThirst.get(), HarderCoreConfig.doTemperature.get(), HarderCoreConfig.doAirQuality.get()));
        }
    }
}
