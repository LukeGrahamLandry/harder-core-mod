package com.lukegraham.hardercore.capability.temp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class TemperaturePacket {
    private UUID entityID;
    private int factor;

    public TemperaturePacket(UUID id, int factor) {
        this.entityID = id;
        this.factor = factor;
    }

    public static TemperaturePacket decode(PacketBuffer buf) {
        TemperaturePacket packet = new TemperaturePacket(buf.readUniqueId(), buf.readInt());
        return packet;
    }

    public static void encode(TemperaturePacket packet, PacketBuffer buf) {
        buf.writeUniqueId(packet.entityID);
        buf.writeInt(packet.factor);
    }

    public static void handle(TemperaturePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().world;
            PlayerEntity player = world.getPlayerByUuid(packet.entityID);
            if (player != null)
                TempCapability.setTemp(player, packet.factor);
        });

        ctx.get().setPacketHandled(true);
    }
}
