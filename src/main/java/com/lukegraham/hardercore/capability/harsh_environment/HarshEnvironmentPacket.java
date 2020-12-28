package com.lukegraham.hardercore.capability.harsh_environment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

// might have to split temp and quality packets to avoid a race condition
public class HarshEnvironmentPacket {
    private UUID entityID;
    private int temp;
    private int quality;
    private int thirst;

    public HarshEnvironmentPacket(UUID id, int t, int q, int w) {
        this.entityID = id;
        this.temp = t;
        this.quality = q;
        this.thirst = w;
    }

    public static HarshEnvironmentPacket decode(PacketBuffer buf) {
        HarshEnvironmentPacket packet = new HarshEnvironmentPacket(buf.readUniqueId(), buf.readInt(), buf.readInt(), buf.readInt());
        return packet;
    }

    public static void encode(HarshEnvironmentPacket packet, PacketBuffer buf) {
        buf.writeUniqueId(packet.entityID);
        buf.writeInt(packet.temp);
        buf.writeInt(packet.quality);
        buf.writeInt(packet.thirst);
    }

    public static void handle(HarshEnvironmentPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().world;
            PlayerEntity player = world.getPlayerByUuid(packet.entityID);
            if (player != null) {
                HarshEnvironmentCapability.setTemp(player, packet.temp);
                HarshEnvironmentCapability.setAirQuality(player, packet.quality);
                HarshEnvironmentCapability.setThirst(player, packet.thirst);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
