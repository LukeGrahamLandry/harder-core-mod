package com.lukegraham.hardercore.init;


import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.items.DescribableItem;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HarderCore.MOD_ID);

    public static final RegistryObject<Item> SHADOW_SCALE = createDescriptionItem("shadow_scale", "Dropped by the killer shadows that spawn in darkness");
    public static final RegistryObject<Item> ANTI_SHADOW_CHARM = createDescriptionItem("anti_shadow_charm", "No killer shadows will haunt you with this in your inventory. Explore the darkness freely");
    public static final RegistryObject<Item> SHADOW_FOOD = ITEMS.register("shadow_food", () -> new DescribableItem(new Item.Properties().group(ModItemGroup.instance)
            .maxStackSize(1).food(new Food.Builder().hunger(20).saturation(20)
                    .effect(() -> new EffectInstance(Effects.POISON, 15*20), 1F)
                    .effect(() -> new EffectInstance(Effects.NIGHT_VISION, 16*60*20), 1F).build()),
            "Gives lots of hunger, night vision and brief poison"));


    private static RegistryObject<Item> createBasicItem(String name){
        return ITEMS.register(name, () -> new Item(new Item.Properties().group(ModItemGroup.instance)));
    }

    private static RegistryObject<Item> createDescriptionItem(String name, String description){
        return ITEMS.register(name, () -> new DescribableItem(new Item.Properties().group(ModItemGroup.instance), description));
    }

    // a new creative tab
    public static class ModItemGroup extends ItemGroup {
        public static final ModItemGroup instance = new ModItemGroup(ItemGroup.GROUPS.length, "hardercore");
        private ModItemGroup(int index, String label) {
            super(index, label);
        }

        @Override
        public ItemStack createIcon() {
            return new ItemStack(SHADOW_SCALE.get());
        }
    }

}
