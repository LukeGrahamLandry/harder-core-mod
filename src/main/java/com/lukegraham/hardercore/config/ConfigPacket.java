package com.lukegraham.hardercore.config;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// server -> client
public class ConfigPacket {
    private final boolean doThirst;
    private final boolean doTemperature;
    private final boolean doAirQuality;

    public ConfigPacket(boolean doThirst, boolean doTemperature, boolean doAirQuality) {
        this.doThirst = doThirst;
        this.doTemperature = doTemperature;
        this.doAirQuality = doAirQuality;
    }

    public static ConfigPacket decode(PacketBuffer buf) {
        ConfigPacket packet = new ConfigPacket(buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
        return packet;
    }

    public static void encode(ConfigPacket packet, PacketBuffer buf) {
        buf.writeBoolean(packet.doThirst);
        buf.writeBoolean(packet.doTemperature);
        buf.writeBoolean(packet.doAirQuality);
    }

    public static void handle(ConfigPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientConfigValues.doThirst = packet.doThirst;
            ClientConfigValues.doTemperature = packet.doTemperature;
            ClientConfigValues.doAirQuality = packet.doAirQuality;
        });

        ctx.get().setPacketHandled(true);
    }
}
