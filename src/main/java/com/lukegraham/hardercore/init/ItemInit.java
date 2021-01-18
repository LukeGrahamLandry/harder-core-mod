package com.lukegraham.hardercore.init;


import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.items.*;
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
    public static final RegistryObject<Item> SHADOW_FOOD = ITEMS.register("shadow_food", () -> new DescribableItem(props()
            .maxStackSize(1).food(new Food.Builder().hunger(20).saturation(0)
                    .effect(() -> new EffectInstance(Effects.POISON, 15*20), 1F)
                    .effect(() -> new EffectInstance(Effects.NIGHT_VISION, 16*60*20), 1F).build()),
            "Gives lots of hunger, night vision and brief poison"));
    // public static final RegistryObject<Item> HELP_BOOK = ITEMS.register("help_book", () -> new HelpBook(props()));
    public static final RegistryObject<Item> WATER_BOTTLE = ITEMS.register("water_bottle", () -> new LargeWaterBottleItem(props().maxStackSize(1), false));
    public static final RegistryObject<Item> AUTO_WATER_BOTTLE = ITEMS.register("auto_water_bottle", () -> new LargeWaterBottleItem(props().maxStackSize(1), true));
    public static final RegistryObject<Item> ANTI_HEAT_CHARM = ITEMS.register("anti_heat_charm", () -> new TemperatureResistCharm(props().maxStackSize(1), "heat"));
    public static final RegistryObject<Item> ANTI_COLD_CHARM = ITEMS.register("anti_cold_charm", () -> new TemperatureResistCharm(props().maxStackSize(1), "cold"));
    // public static final RegistryObject<Item> ANTI_TEMP_CHARM = ITEMS.register("anti_temp_charm", () -> new TemperatureResistCharm(props().maxStackSize(1), "heat and cold"));
    public static final RegistryObject<Item> BLOOD_MOON_TOTEM = ITEMS.register("blood_moon_totem", () -> new BloodMoonTotem(props().maxStackSize(1)));
    public static final RegistryObject<Item> OXYGEN_CANISTER = ITEMS.register("oxygen_canister", () -> new OxygenCanister(props().maxStackSize(1), false));

    private static RegistryObject<Item> createDescriptionItem(String name, String description){
        return ITEMS.register(name, () -> new DescribableItem(props(), description));
    }

    private static Item.Properties props(){
        return new Item.Properties().group(ModItemGroup.instance);
    }

    // a new creative tab
    public static class ModItemGroup extends ItemGroup {
        public static final ModItemGroup instance = new ModItemGroup(ItemGroup.GROUPS.length, "hardercore");
        private ModItemGroup(int index, String label) {
            super(index, label);
        }

        @Override
        public ItemStack createIcon() {
            return new ItemStack(BLOOD_MOON_TOTEM.get());
        }
    }

}
