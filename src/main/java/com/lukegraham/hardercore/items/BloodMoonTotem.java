package com.lukegraham.hardercore.items;

import com.lukegraham.hardercore.events.BloodMoonHandler;
import com.lukegraham.hardercore.util.KeyboardHelper;
import com.lukegraham.hardercore.world_data.HarderCoreData;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public class BloodMoonTotem extends Item {
    public BloodMoonTotem(Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (KeyboardHelper.isHoldingShift()) {
            tooltip.add(new StringTextComponent("Shift right click to summon a blood moon"));
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (handIn != Hand.MAIN_HAND || !KeyboardHelper.isHoldingShift()) return super.onItemRightClick(worldIn, playerIn, handIn);

        ItemStack stack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote()){
            stack.shrink(1);
            ((ServerWorld)worldIn).func_241114_a_(13000);
            while (!BloodMoonHandler.isBloodMoon(worldIn)){
                HarderCoreData.getInstance(worldIn).increaseSleeps();
            }
        }

        return ActionResult.resultSuccess(stack);
    }
}
