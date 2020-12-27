package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.entities.ShadowKillerEntity;
import com.lukegraham.hardercore.init.EffectInit;
import com.lukegraham.hardercore.init.ItemInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExhaustionHandler {
    private static final Random rand = new Random();

    @SubscribeEvent
    public static void checkAndApplyExhaustionRandomly(TickEvent.PlayerTickEvent event){
        if (rand.nextInt(10000) == 0){
            applyExhaustion(event.player);
        }
    }

    @SubscribeEvent
    public static void removeExhaustionOnSleep(SleepFinishedTimeEvent event){
        event.getWorld().getPlayers().forEach(ExhaustionHandler::applyExhaustion);
    }

    private static void applyExhaustion(PlayerEntity player){
        if (player.getEntityWorld().isRemote()) return;

        int level = calculateExhaustionLevel(player);
        if (level >= 0){
            player.addPotionEffect(new EffectInstance(EffectInit.EXHAUSTION.get(), Integer.MAX_VALUE, level, true, false));
            if (level > 10){
                player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 50*level, 1, true, false));
            }
        } else if (player.getActivePotionEffect(EffectInit.EXHAUSTION.get()) != null){
            player.removePotionEffect(EffectInit.EXHAUSTION.get());
        }
    }

    private static int calculateExhaustionLevel(PlayerEntity player){
        int ticksSinceSleep = MathHelper.clamp(((ServerPlayerEntity)player).getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);
        HarderCore.LOGGER.debug(ticksSinceSleep + " ticks since sleep");
        int gameDaysSinceSleep = (int) Math.floor(ticksSinceSleep / 24000.0D);
        return gameDaysSinceSleep - 2;
    }
}
