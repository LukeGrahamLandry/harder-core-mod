package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.init.EffectInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HungryHandler {
    private static final Random rand = new Random();

    @SubscribeEvent
    public static void checkAndApplyHungryRandomly(TickEvent.PlayerTickEvent event){
        if (rand.nextInt(1000) == 0){  // TODO: lower the number after testing
            applyHungry(event.player);
        }
    }

    @SubscribeEvent
    public static void removeHungryOnEat(LivingEntityUseItemEvent.Finish event){
        if (event.getItem().isFood() && event.getEntityLiving() instanceof PlayerEntity){
            applyHungry((PlayerEntity) event.getEntityLiving());

        }
    }

    private static void applyHungry(PlayerEntity player){
        if (player.getEntityWorld().isRemote()) return;

        int level = calculateHungryLevel(player);
        if (level >= 0){
            player.removePotionEffect(EffectInit.HUNGRY.get());  // remove first in case it went down since last time
            player.addPotionEffect(new EffectInstance(EffectInit.HUNGRY.get(), Integer.MAX_VALUE, level, true, false));
        } else if (player.getActivePotionEffect(EffectInit.HUNGRY.get()) != null){
            player.removePotionEffect(EffectInit.HUNGRY.get());
        }
    }

    private static int calculateHungryLevel(PlayerEntity player){
        int hungerMissing = 20 - player.getFoodStats().getFoodLevel();
        int level = (int) Math.floor(hungerMissing / 5.0D);
        return level - 1;
    }
}
