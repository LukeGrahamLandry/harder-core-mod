package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironmentCapability;
import com.lukegraham.hardercore.init.EffectInit;
import com.lukegraham.hardercore.util.Helper;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.BrewingStandTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AirQualityHandler {
    public static void increaseAirQualityOverTime(PlayerEntity player){
        int amount = calculateTotalQualityShift(player);
        changeAirQualityAndRecalculateEffects(player, amount);
        HarderCore.LOGGER.debug("air quality increased " + amount);
    }

    public static int calculateTotalQualityShift(PlayerEntity player){
        int amount = isOutside(player) ? -3 : -1;
        if (player.getEntityWorld().getBiome(player.getPosition()).getCategory() == Biome.Category.NETHER || player.areEyesInFluid(FluidTags.WATER)){
            amount = 1;
        }

        amount += getProximityQualityModifiers(player);

        return amount;
    }

    private static int getProximityQualityModifiers(PlayerEntity player) {
        int size = 3;
        int amount = 0;
        for (int x=size*-1;x<=size;x++){
            for (int y=0;y<=size;y++){
                for (int z=size*-1;z<=size;z++){
                    BlockPos pos = player.getPosition().add(x, y, z);
                    amount += getBlockQualityModifier(player.world, pos);
                }
            }
        }

        List<Entity> blazes = player.getEntityWorld().getEntitiesWithinAABB(BlazeEntity.class, Helper.getAABB(player.getPosition(), size));
        amount += blazes.size();

        return amount;
    }

    private static int getBlockQualityModifier(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof TorchBlock || block instanceof AbstractFireBlock) return 1;
        if (block instanceof AbstractFurnaceBlock || block == Blocks.CAMPFIRE){
            if (state.get(AbstractFurnaceBlock.LIT)) return 3;
        }
        return 0;
    }

    private static boolean isOutside(PlayerEntity player){
        BlockPos blockpos = player.getRidingEntity() instanceof BoatEntity ? (new BlockPos(player.getPosX(), (double)Math.round(player.getPosY()), player.getPosZ())).up() : new BlockPos(player.getPosX(), (double)Math.round(player.getPosY()), player.getPosZ());
        return player.world.canSeeSky(blockpos);
    }

    private static void changeAirQualityAndRecalculateEffects(PlayerEntity player, int amount){
        if (player.getEntityWorld().isRemote() || player.isCreative() || player.isSpectator()) return;

        HarshEnvironmentCapability.addAirQuality(player, amount);

        int quality = HarshEnvironmentCapability.getAirQuality(player);
        player.removePotionEffect(EffectInit.BAD_AIR.get());
        int level = calculateEffectLevel(quality);
        if (level == -1) return;
        player.addPotionEffect(new EffectInstance(EffectInit.BAD_AIR.get(), Integer.MAX_VALUE, level, true, false));
    }

    public static int calculateEffectLevel(int q){
        if (q < 40) return -1;
        if (q < 60) return 0;
        if (q < 80) return 1;
        if (q < 100) return 2;
        if (q == 100) return 3;
        return 4;
    }
}
