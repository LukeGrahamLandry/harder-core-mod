package com.lukegraham.hardercore.capability.harsh_environment;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.util.PacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

public class HarshEnvironmentCapability {
    @CapabilityInject(IHarshEnvironment.class)
    public static Capability<IHarshEnvironment> TEMP_CAPABILITY;

    public static final Direction DEFAULT_FACING = null;

    public static LazyOptional<IHarshEnvironment> getPlayerTempCap(final PlayerEntity player) {
        return player.getCapability(TEMP_CAPABILITY, DEFAULT_FACING);
    }

    public static void addTemp(final PlayerEntity player, int amount){
        getPlayerTempCap(player).ifPresent(cap -> cap.addTemp(amount));
        sendUpdatePacket(player);
    }

    public static int getTemp(final PlayerEntity player) {
        if (player != null && player.isAlive()) return getPlayerTempCap(player).map(IHarshEnvironment::getTemp).orElse(0);
        return 0;
    }
    public static void setTemp(final PlayerEntity player, final int amount){
        getPlayerTempCap(player).ifPresent(cap -> cap.setTemp(amount));
        sendUpdatePacket(player);
    }

    public static void addAirQuality(final PlayerEntity player, int amount){
        getPlayerTempCap(player).ifPresent(cap -> cap.addAirQuality(amount));
        sendUpdatePacket(player);
    }

    public static int getAirQuality(final PlayerEntity player) {
        if (player != null && player.isAlive()) return getPlayerTempCap(player).map(IHarshEnvironment::getAirQuality).orElse(0);
        return 0;
    }
    public static void setAirQuality(final PlayerEntity player, final int amount){
        getPlayerTempCap(player).ifPresent(cap -> cap.setAirQuality(amount));
        sendUpdatePacket(player);
    }

    public static void addThirst(final PlayerEntity player, int amount){
        getPlayerTempCap(player).ifPresent(cap -> cap.addThirst(amount));
        sendUpdatePacket(player);
    }

    public static int getThirst(final PlayerEntity player) {
        if (player != null && player.isAlive()) return getPlayerTempCap(player).map(IHarshEnvironment::getThirst).orElse(0);
        return 0;
    }
    public static void setThirst(final PlayerEntity player, final int amount){
        getPlayerTempCap(player).ifPresent(cap -> cap.setThirst(amount));
        sendUpdatePacket(player);
    }

    public static void sendUpdatePacket(PlayerEntity player){
        HarderCore.LOGGER.debug(HarshEnvironmentCapability.getTemp(player) + " " + HarshEnvironmentCapability.getAirQuality(player) + " " + HarshEnvironmentCapability.getThirst(player));
        if (!player.getEntityWorld().isRemote()){

            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                    new HarshEnvironmentPacket(player.getUniqueID(),
                            HarshEnvironmentCapability.getTemp(player),
                            HarshEnvironmentCapability.getAirQuality(player),
                            HarshEnvironmentCapability.getThirst(player)));
        }
    }
}
