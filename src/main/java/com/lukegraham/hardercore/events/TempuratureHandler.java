package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.capability.temp.TempCapProvider;
import com.lukegraham.hardercore.capability.temp.TempCapability;
import com.lukegraham.hardercore.capability.temp.TemperaturePacket;
import com.lukegraham.hardercore.init.EffectInit;
import com.lukegraham.hardercore.util.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TempuratureHandler {
    private static final Random rand = new Random();

    @SubscribeEvent
    public static void applyBiomeTemp(TickEvent.PlayerTickEvent event){
        // increase time between updates after testing
        if (rand.nextInt(100) == 0 && !event.player.getEntityWorld().isRemote()) {
            PlayerEntity player = event.player;
            int tempShift = getBiomeTempShift(player);
            if (player.isInWater()) tempShift -= 10;
            if (player.getFireTimer() > 0) tempShift += 20;
            if (player.getPosY() > 100) tempShift -= 5;
            TempCapability.addTemp(player, tempShift);

            HarderCore.LOGGER.debug(TempCapability.getTemp(event.player));
        }
    }

    private static int getBiomeTempShift(PlayerEntity player){
        Biome biome = player.getEntityWorld().getBiome(player.getPosition());
        if (biome.getCategory() == Biome.Category.NETHER){
            return 10;
        }
        if (biome.getCategory() == Biome.Category.DESERT || biome.getCategory() == Biome.Category.MESA){
            return 5;
        }
        if (biome.getCategory() == Biome.Category.JUNGLE || biome.getCategory() == Biome.Category.SAVANNA){
            return 2;
        }
        if (biome.getCategory() == Biome.Category.SWAMP){
            return 1;
        }
        if (biome.getCategory() == Biome.Category.TAIGA){
            return -2;
        }
        if (biome.getCategory() == Biome.Category.ICY){
            return -5;
        }
        if (biome.getCategory() == Biome.Category.THEEND){
            return -10;
        }

        return 0;
    }

    @SubscribeEvent
    public static void attachCap(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity){
            ICapabilityProvider cap = new TempCapProvider();
            event.addCapability(new ResourceLocation(HarderCore.MOD_ID, "tempurature"), cap);
        }
    }
}
