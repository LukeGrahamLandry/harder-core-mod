package com.lukegraham.hardercore.util;


import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.capability.temp.TemperaturePacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    public static SimpleChannel INSTANCE;

    public static void registerMessages(String channelName) {
        int i = -1;
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(HarderCore.MOD_ID, channelName), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(++i, TemperaturePacket.class,
                TemperaturePacket::encode,
                TemperaturePacket::decode,
                TemperaturePacket::handle);
    }
}