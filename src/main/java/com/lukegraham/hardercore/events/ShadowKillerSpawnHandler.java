package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.entities.ShadowKillerEntity;
import com.lukegraham.hardercore.init.EntityInit;
import com.lukegraham.hardercore.init.ItemInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.ServerStatisticsManager;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ShadowKillerSpawnHandler {
    public static void spawnShadowKillersInDarkness(PlayerEntity player){
        World world = player.getEntityWorld();
        if (world.isRemote() || player.isCreative() || player.isSpectator()) return;
        BlockPos pos = player.getPosition();

        int light = world.getLight(pos);
        if (light <= 2 && !hasAntiCharm(player) && !world.canSeeSky(player.getPosition()) && !player.isInWater()){
            boolean isOverworld = player.getEntityWorld().getBiome(player.getPosition()).getCategory() != Biome.Category.NETHER && player.getEntityWorld().getBiome(player.getPosition()).getCategory() != Biome.Category.THEEND;
            if (isOverworld) ShadowKillerEntity.summon(player);
        }
    }

    private static boolean hasAntiCharm(PlayerEntity player){
        boolean hasCharm = false;
        for (ItemStack stack : player.inventory.mainInventory){
            if (stack.getItem() == ItemInit.ANTI_SHADOW_CHARM.get()) hasCharm = true;
        }
        if (player.getHeldItem(Hand.OFF_HAND).getItem() == ItemInit.ANTI_SHADOW_CHARM.get()) hasCharm = true;

        boolean hasTorch = false;
        if (player.getHeldItem(Hand.OFF_HAND).getItem() == Items.TORCH ||
                player.getHeldItem(Hand.MAIN_HAND).getItem() == Items.TORCH ||
                player.getHeldItem(Hand.OFF_HAND).getItem() == Items.LANTERN ||
                player.getHeldItem(Hand.MAIN_HAND).getItem() == Items.LANTERN) hasTorch = true;

        return hasCharm || hasTorch;
    }
}
