package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.config.HarderCoreConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FoodPoisoningHandler {
    private static final Random rand = new Random();

    @SubscribeEvent
    public static void foodPoisoningOnEat(LivingEntityUseItemEvent.Finish event){
        LivingEntity entity = event.getEntityLiving();
        if (!entity.getEntityWorld().isRemote()){
            if (HarderCoreConfig.doFoodPoisoning.get() && isRaw(event.getItem())){
                int scale = rand.nextInt(15);
                int amp = rand.nextInt(10) == 0? 1: 0;
                entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 100 + (30 * scale), amp));
                if (rand.nextInt(2) == 0){
                    entity.addPotionEffect(new EffectInstance(Effects.POISON, 100 + (20 * scale), 0));
                } else {
                    entity.addPotionEffect(new EffectInstance(Effects.HUNGER, 200 + (20 * scale), 0));
                }
            }
        }
    }

    private static boolean isRaw(ItemStack stack){
        Ingredient raw = Ingredient.fromItems(Items.BEEF, Items.MUTTON, Items.CHICKEN, Items.PORKCHOP, Items.COD, Items.SALMON, Items.RABBIT, Items.TROPICAL_FISH, Items.PUFFERFISH, Items.ROTTEN_FLESH);
        return raw.test(stack);
    }
}
