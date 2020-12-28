package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironmentCapability;
import com.lukegraham.hardercore.init.EffectInit;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AirQualityHandler {
    private static final Random rand = new Random();

    @SubscribeEvent
    public static void reduceAirQualityOnSmelt(PlayerEvent.ItemSmeltedEvent event){
        PlayerEntity player = event.getPlayer();
        if (!player.getEntityWorld().isRemote()){
            int q = (int) (Math.floor(event.getSmelting().getCount() / 8.0D) + 1);
            changeAirQualityAndRecalculateEffects(player, q);
        }
    }

    @SubscribeEvent
    public static void reduceAirQualityOnTorchPlace(BlockEvent.EntityPlaceEvent event){
        boolean isPlayer = event.getEntity() instanceof PlayerEntity;
        boolean isTorch = event.getPlacedBlock().getBlock() == Blocks.TORCH || event.getPlacedBlock().getBlock() == Blocks.WALL_TORCH;
        if (isPlayer && isTorch){
            changeAirQualityAndRecalculateEffects((PlayerEntity) event.getEntity(), 2);
        }

    }

    @SubscribeEvent
    public static void increaseAirQualityOverTime(TickEvent.PlayerTickEvent event){
        if (!event.player.getEntityWorld().isRemote() && rand.nextInt(200) == 0) {
            PlayerEntity player = event.player;
            int amount = isOutside(player) ? -3 : -1;
            if (event.player.getEntityWorld().getBiome(event.player.getPosition()).getCategory() == Biome.Category.NETHER){
                amount = 1;
            }
            changeAirQualityAndRecalculateEffects(player, amount);
        }
    }

    private static boolean isOutside(PlayerEntity player){
        BlockPos blockpos = player.getRidingEntity() instanceof BoatEntity ? (new BlockPos(player.getPosX(), (double)Math.round(player.getPosY()), player.getPosZ())).up() : new BlockPos(player.getPosX(), (double)Math.round(player.getPosY()), player.getPosZ());
        return player.world.canSeeSky(blockpos);
    }

    private static void changeAirQualityAndRecalculateEffects(PlayerEntity player, int amount){
        if (player.getEntityWorld().isRemote()) return;

        HarshEnvironmentCapability.addAirQuality(player, amount);

        int quality = HarshEnvironmentCapability.getAirQuality(player);
        player.removePotionEffect(EffectInit.BAD_AIR.get());
        int level = calculateEffectLevel(quality);
        HarderCore.LOGGER.debug(quality + "% -> " + level);
        if (level == -1) return;
        player.addPotionEffect(new EffectInstance(EffectInit.BAD_AIR.get(), Integer.MAX_VALUE, level, true, false));
    }

    public static int calculateEffectLevel(int q){
        if (q < 20) return -1;
        return (int) Math.floor(q / 20.0D) - 1;
    }
}
