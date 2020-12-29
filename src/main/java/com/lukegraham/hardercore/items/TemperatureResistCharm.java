package com.lukegraham.hardercore.items;

import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironment;
import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironmentCapability;
import com.lukegraham.hardercore.util.KeyboardHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class TemperatureResistCharm extends Item {
    private final String type;
    public TemperatureResistCharm(Properties properties, String typeIn) {
        super(properties);
        this.type = typeIn;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (KeyboardHelper.isHoldingShift()) {
            tooltip.add(new StringTextComponent("Grants immunity to the effects of " + this.type));
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote() && entityIn instanceof PlayerEntity && random.nextInt(50) == 0) {
            int temp = HarshEnvironmentCapability.getTemp((PlayerEntity) entityIn);
            if (type.equals("heat and cold")){
                HarshEnvironmentCapability.setTemp((PlayerEntity) entityIn, 0);
            } else {
                if (type.equals("heat") && temp > 0){
                    HarshEnvironmentCapability.setTemp((PlayerEntity) entityIn, 0);
                } else if (temp < 0){
                    HarshEnvironmentCapability.setTemp((PlayerEntity) entityIn, 0);
                }
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }
}
