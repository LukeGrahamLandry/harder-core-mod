package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironment;
import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironmentCapability;
import com.lukegraham.hardercore.init.EffectInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ThirstHandler {
    private static final Random rand = new Random();

    public static void checkAndApplyThirst(PlayerEntity player){
        int amount = player.isInWater() ? -5 : 1;
        int temp = TempuratureHandler.getBiomeTempShift(player);
        if (temp > 0 && amount > 0 && rand.nextInt(2) == 0){
            if (temp > 6) amount = 3;
            else if (temp > 3) amount = 2;
        }
        HarshEnvironmentCapability.addThirst(player, amount);
        applyNegativeEffects(player);
    }

    @SubscribeEvent
    public static void removeThirstOnDrink(LivingEntityUseItemEvent.Finish event){
        boolean isLiquid = event.getItem().getItem() == Items.POTION || event.getItem().getItem() == Items.MILK_BUCKET || event.getItem().getItem() == Items.MELON_SLICE;
        if (isLiquid && event.getEntityLiving() instanceof PlayerEntity && !event.getEntityLiving().getEntityWorld().isRemote()){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();

            HarshEnvironmentCapability.addThirst(player, -10);
            applyNegativeEffects(player);
            if (HarshEnvironmentCapability.getTemp(player) > 0) HarshEnvironmentCapability.addTemp(player, -10);
        }
    }

    private static void applyNegativeEffects(PlayerEntity player){
        int thirst = HarshEnvironmentCapability.getThirst(player);
        int level = calculateEffectLevel(thirst);
        player.removePotionEffect(EffectInit.THIRST.get());
        if (level >= 0){
            player.addPotionEffect(new EffectInstance(EffectInit.THIRST.get(), Integer.MAX_VALUE, level, true, false));
        }
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
