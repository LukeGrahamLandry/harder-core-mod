package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironmentCapProvider;
import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironmentCapability;
import com.lukegraham.hardercore.init.EffectInit;
import com.lukegraham.hardercore.util.Helper;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TempuratureHandler {
    public static void updatePlayerTemp(PlayerEntity player){
        int temp = updateCurrentTemp(player);
        applyNegativeEffects(player, temp);
    }

    private static void applyNegativeEffects(PlayerEntity player, int temp){
        player.removePotionEffect(EffectInit.HEAT_STROKE.get());
        player.removePotionEffect(EffectInit.HYPOTHERMIA.get());
        int level = calculateEffectLevel(temp);
        if (level == -1) return;
        Effect effect = temp > 0 ? EffectInit.HEAT_STROKE.get() : EffectInit.HYPOTHERMIA.get();
        if (temp > 0 && player.getActivePotionEffect(Effects.FIRE_RESISTANCE) != null) return;
        player.addPotionEffect(new EffectInstance(effect, Integer.MAX_VALUE, level, true, false));
    }

    private static int calculateEffectLevel(int temp){
        int t = Math.abs(temp);
        if (t < 100) return -1;
        return (int) Math.floor(t / 100.0D) - 1;
    }

    private static int updateCurrentTemp(PlayerEntity player){
        int oldTemp = HarshEnvironmentCapability.getTemp(player);
        int tempShift = calculateTotalTempShift(player);
        HarderCore.LOGGER.debug("temp increased " + tempShift);

        HarshEnvironmentCapability.addTemp(player, tempShift);
        return oldTemp + tempShift;
    }

    public static int calculateTotalTempShift(PlayerEntity player){
        int tempShift = getBiomeTempShift(player);
        int oldTemp = HarshEnvironmentCapability.getTemp(player);

        if (player.isInWater()){
            if (oldTemp > 0) tempShift -= 10;
            else if (tempShift < 0) tempShift -= 10;
        } else if (tempShift < 0) {
            tempShift += calculateArmorWarmth(player);
        }
        if (player.getFireTimer() > 0) tempShift += 25;
        if (player.getPosY() > 100) tempShift -= 5;
        if (player.getPosY() < 10) tempShift += 5;
        tempShift += getProximityQualityModifiers(player);
        if (tempShift > 0 && player.getActivePotionEffect(Effects.FIRE_RESISTANCE) != null) tempShift = 0;

        if (tempShift == 0){
            if (oldTemp > 0) tempShift = -1;
            if (oldTemp < 0) tempShift = 1;
        }

        if (Math.signum(tempShift) != Math.signum(oldTemp)){
            if (oldTemp > 0) tempShift = -5;
            if (oldTemp < 0) tempShift = 5;
        }



        return tempShift;
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

        return amount;
    }

    private static int getBlockQualityModifier(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Fluid fluid = world.getFluidState(pos).getFluid();
        Block block = state.getBlock();
        if (block instanceof AbstractFireBlock || fluid == Fluids.LAVA) return 1;
        if (block instanceof AbstractFurnaceBlock || block == Blocks.CAMPFIRE){
            if (state.get(AbstractFurnaceBlock.LIT)) return 2;
        }
        if (block == Blocks.ICE || block == Blocks.PACKED_ICE || block == Blocks.BLUE_ICE) return -1;
        return 0;
    }

    private static int calculateArmorWarmth(PlayerEntity player){
        int clothesBonus = 0;
        for (ItemStack stack : player.inventory.armorInventory){
            Item item = stack.getItem();
            if (item instanceof ArmorItem){
                boolean isLeather = ((ArmorItem) item).getArmorMaterial().getRepairMaterial().test(new ItemStack(Items.LEATHER));
                if (isLeather) clothesBonus += 5;
                else
                    clothesBonus += 2;
            }
        }
        return clothesBonus;
    }

    public static int getBiomeTempShift(PlayerEntity player){
        Biome biome = player.getEntityWorld().getBiome(player.getPosition());

        if (biome.getCategory() == Biome.Category.NETHER){
            return 10;
        }
        if (biome.getCategory() == Biome.Category.DESERT || biome.getCategory() == Biome.Category.MESA){
            if (player.getEntityWorld().isNightTime()) return -2;
            else return 5;
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
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity){
            ICapabilityProvider cap = new HarshEnvironmentCapProvider();
            event.addCapability(new ResourceLocation(HarderCore.MOD_ID, "temp"), cap);
        }
    }
}
