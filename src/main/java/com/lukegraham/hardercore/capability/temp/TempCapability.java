package com.lukegraham.hardercore.capability.temp;

import com.lukegraham.hardercore.util.PacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

public class TempCapability {
    @CapabilityInject(ITemp.class)
    public static Capability<ITemp> TEMP_CAPABILITY;

    public static final Direction DEFAULT_FACING = null;

    public static LazyOptional<ITemp> getPlayerTempCap(final PlayerEntity player) {
        return player.getCapability(TEMP_CAPABILITY, DEFAULT_FACING);
    }

    public static void addTemp(final PlayerEntity player, int amount){
        getPlayerTempCap(player).ifPresent(cap -> cap.addTemp(amount));
        if (!player.getEntityWorld().isRemote())
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new TemperaturePacket(player.getUniqueID(), TempCapability.getTemp(player)));
    }

    public static int getTemp(final PlayerEntity player) {
        if (player != null && player.isAlive()) return getPlayerTempCap(player).map(ITemp::getTemp).orElse(0);
        return 0;
    }
    public static void setTemp(final PlayerEntity player, final int amount){
        getPlayerTempCap(player).ifPresent(cap -> cap.setTemp(amount));
        if (!player.getEntityWorld().isRemote())
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new TemperaturePacket(player.getUniqueID(), TempCapability.getTemp(player)));
    }
}
