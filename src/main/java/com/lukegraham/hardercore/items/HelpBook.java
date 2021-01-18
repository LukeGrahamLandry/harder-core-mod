package com.lukegraham.hardercore.items;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.init.ItemInit;
import com.lukegraham.hardercore.util.KeyboardHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ReadBookScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
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

        addPage("This mod (Harder Core) makes many changes to make the game harder and the world less forgiving. The rest of this book lists the changes. If you're interested in the details, this mod is open source: github.com/LukeGrahamLandry/harder-core-mod", pages);
        addPage("Being hungry causes slowness and weakness. Not sleeping causes slowness, reduced attack speed and eventually some nausea. Eating raw meat gives nausea and either poison or hunger.", pages);
        addPage("Being in darkness quickly spawns killer shadows which disappear in light. They drop scales which can craft a charm that stops their spawning while in your inventory or a questionable stew.", pages);
        addPage("Your temperature (displayed in top left) effected by biome, armor, being on fire and nearby blocks. Extremes give you hypothermia (slow move/attack) or heat stroke (nausea) both cause weakness and deal damage. You can craft charms that grant temperature immunity.", pages);
        addPage("Your air quality (displayed above food) is reduced by proximity to torches or furnaces and raised by time spent outside. Drink from an oxygen canister to recover air quality (or breath bubbles) quickly. Too low quality causes damage, nausea and blindness.", pages);
        addPage("Your water level (displayed above food) deceases over time, more in hot biomes. Drink water bottles, potions, milk, swim or eat melon) to increase. Hold lots of water in a hydro flask (upgrade your flask to auto drink). Too low causes nausea and damage.", pages);
        addPage("There's a chance for buffed mobs to spawn. Some give poison or fire on hit. Some dodge projectiles (like endermen).", pages);
        addPage("A blood moon happens once every 8 nights. Hoards of monsters spawn and you cannot sleep. Can be summoned early with a Totem of the Blood Moon.", pages);
        addPage("When you die a gravestone will spawn (along with a deadly skeleton). Right click it to return your items to the same slots of your inventory or break it to drop them. It is immune to explosions and if you die in a block or the void it will move upwards to the closest air/fluid.", pages);

        return pages;
    }

    private void addPage(String content, ListNBT pages){
        INBT page = (INBT) StringNBT.valueOf("{\"text\": \"" + content + "\"}");
        // INBT page = (INBT) StringNBT.valueOf(ITextComponent.Serializer.toJson(itextcomponent));
        pages.add(page);
    }

        private static final String NBT_KEY = HarderCore.MOD_ID + ".first_joined";


        // Give the book the first time they join the world
        @SubscribeEvent
        public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            PlayerEntity player = event.getPlayer();
            if (player instanceof ServerPlayerEntity) {

                CompoundNBT data = player.getPersistentData();
                CompoundNBT persistent;
                if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
                    data.put(PlayerEntity.PERSISTED_NBT_TAG, (persistent = new CompoundNBT()));
                } else {
                    persistent = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
                }

                if (!persistent.contains(NBT_KEY)) {
                    persistent.putBoolean(NBT_KEY, true);

                    // ItemStack stack = new ItemStack(ItemInit.HELP_BOOK.get());
                    // player.inventory.addItemStackToInventory(stack);
                }
            }
        }
}
