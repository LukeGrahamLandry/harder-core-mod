package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.init.EffectInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HungryHandler {
    private static final Random rand = new Random();

    @SubscribeEvent
    public static void removeHungryOnEat(LivingEntityUseItemEvent.Finish event){
        if (event.getItem().isFood() && event.getEntityLiving() instanceof PlayerEntity){
            checkAndApplyHungry((PlayerEntity) event.getEntityLiving());
        }
    }

    public static void checkAndApplyHungry(PlayerEntity player){
        if (player.getEntityWorld().isRemote() || player.isCreative() || player.isSpectator()) return;

        int level = calculateHungryLevel(player);
        player.removePotionEffect(EffectInit.HUNGRY.get());
        if (level >= 0){
            player.addPotionEffect(new EffectInstance(EffectInit.HUNGRY.get(), Integer.MAX_VALUE, level, true, false));
        }
    }

    private static int calculateHungryLevel(PlayerEntity player){
        int hungerMissing = 20 - player.getFoodStats().getFoodLevel();
        int level = (int) Math.floor(hungerMissing / 6.0D);
        return level - 1;
    }
}
