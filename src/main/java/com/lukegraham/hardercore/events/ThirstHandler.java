package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironmentCapability;
import com.lukegraham.hardercore.init.EffectInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ThirstHandler {
    private static final Random rand = new Random();

    @SubscribeEvent
    public static void checkAndApplyThirstRandomly(TickEvent.PlayerTickEvent event){
        if (!event.player.getEntityWorld().isRemote() && rand.nextInt(200) == 0){
            int amount = event.player.isInWater() ? -1 : 1;
            int temp = TempuratureHandler.getBiomeTempShift(event.player);
            if (temp > 0 && amount > 0){
                if (temp > 6) amount = 4;
                else if (temp > 3) amount = 3;
                else amount = 2;
            }
            HarshEnvironmentCapability.addThirst(event.player, amount);
            applyThirst(event.player);
        }
    }

    @SubscribeEvent
    public static void removeThirstOnDrink(LivingEntityUseItemEvent.Finish event){
        boolean isLiquid = event.getItem().getItem() == Items.POTION || event.getItem().getItem() == Items.MILK_BUCKET || event.getItem().getItem() == Items.MELON_SLICE;
        if (isLiquid && event.getEntityLiving() instanceof PlayerEntity && !event.getEntityLiving().getEntityWorld().isRemote()){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            HarshEnvironmentCapability.addThirst(player, -10);
            applyThirst(player);
        }
    }

    private static void applyThirst(PlayerEntity player){
        if (player.getEntityWorld().isRemote()) return;

        int thirst = HarshEnvironmentCapability.getThirst(player);
        int level = AirQualityHandler.calculateEffectLevel(thirst);
        player.removePotionEffect(EffectInit.THIRST.get());
        if (level >= 0){
            player.addPotionEffect(new EffectInstance(EffectInit.THIRST.get(), Integer.MAX_VALUE, level, true, false));
        }
    }
}
