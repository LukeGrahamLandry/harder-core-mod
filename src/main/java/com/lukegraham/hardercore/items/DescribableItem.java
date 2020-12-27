package com.lukegraham.hardercore.items;

import com.lukegraham.hardercore.util.KeyboardHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class DescribableItem extends Item {
    String desc;
    public DescribableItem(Properties properties, String descIn) {
        super(properties);
        this.desc = descIn;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (KeyboardHelper.isHoldingShift()) {
            tooltip.add(new StringTextComponent(this.desc));
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
