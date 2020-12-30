package com.lukegraham.hardercore.init;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.tile_entity.GraveTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInit {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, HarderCore.MOD_ID);

    public static final RegistryObject<TileEntityType<GraveTileEntity>> GRAVE
            = TILE_ENTITY_TYPES.register("grave",
            () -> TileEntityType.Builder.create(GraveTileEntity::new, BlockInit.GRAVE.get()).build(null));

}
