package com.lukegraham.hardercore.items;

import com.lukegraham.hardercore.util.KeyboardHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ReadBookScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WritableBookItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class HelpBook extends WritableBookItem {
    public HelpBook(Properties builder) {
        super(builder);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (KeyboardHelper.isHoldingShift()) {
            tooltip.add(new StringTextComponent("Learn about the features of Harder Core"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        setContents(stack);

        Minecraft.getInstance().displayGuiScreen(new ReadBookScreen(new ReadBookScreen.WrittenBookInfo(stack)));

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private void setContents(ItemStack stack){
        CompoundNBT tag = stack.getTag();
        if (tag == null) tag = new CompoundNBT();

        tag.putString("author", "Luke");
        tag.putString("title", "Harder Core Help");
        tag.putBoolean("resolved", true);

        ListNBT pages = getPages();
        tag.put("pages", pages);

        stack.setTag(tag);
    }

    private ListNBT getPages(){
        ListNBT pages = new ListNBT();

        addPage("This mod makes some changes to make the world less forgiving. Being hungry causes slowness and weakness. Not sleeping causes slowness, reduced attack speed and eventually some nausea. Eating raw meat gives nausea and either poison or hunger.", pages);
        addPage("Being in darkness quickly spawns killer shadows which disappear in light. They drop scales which can craft a charm that stops their spawning while in your inventory or a questionable stew.", pages);
        addPage("Your temperature (displayed in top left) depends on current biome, armor and being in water or on fire. Extremes give you hypothermia (slow move/attack) or heat stroke (nausea) both cause weakness and deal damage. ", pages);
        addPage("Your air quality (displayed in top left) is reduced by smelting or placed torches and raised by time spent outside. Too low quality causes nausea, hunger and damage", pages);
        addPage("Your water level (displayed in top left) deceases over time, more in hot biomes. Drink water bottles, potions, milk (or go swimming or eat melon) to increase. Too low causes nausea and damage", pages);
        addPage("", pages);
        addPage("", pages);
        return pages;
    }

    private void addPage(String content, ListNBT pages){
        INBT page = (INBT) StringNBT.valueOf("{\"text\": \"" + content + "\"}");
        // INBT page = (INBT) StringNBT.valueOf(ITextComponent.Serializer.toJson(itextcomponent));
        pages.add(page);
    }
}
