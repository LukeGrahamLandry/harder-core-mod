package com.lukegraham.hardercore.init;


import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.entities.ShadowKillerEntity;
import com.lukegraham.hardercore.entities.WonderingSpiritEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityInit {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, HarderCore.MOD_ID);

    public static final RegistryObject<EntityType<ShadowKillerEntity>> SHADOW_KILLER = ENTITY_TYPES.register("shadow_killer",
            () -> EntityType.Builder.create(ShadowKillerEntity::new, EntityClassification.MONSTER).size(0.8f, 0.8f)
                    .build(HarderCore.MOD_ID + ":shadow_killer"));

    public static final RegistryObject<EntityType<WonderingSpiritEntity>> WONDERING_SPIRIT = ENTITY_TYPES.register("wondering_spirit",
            () -> EntityType.Builder.create(WonderingSpiritEntity::new, EntityClassification.MONSTER).size(0.6F, 1.95F)
                    .build(HarderCore.MOD_ID + ":wondering_spirit"));
}
