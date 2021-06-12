package com.lukegraham.hardercore;

import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironment;
import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironmentStorage;
import com.lukegraham.hardercore.capability.harsh_environment.IHarshEnvironment;
import com.lukegraham.hardercore.entities.ShadowKillerEntity;
import com.lukegraham.hardercore.entities.WonderingSpiritEntity;
import com.lukegraham.hardercore.init.*;
import com.lukegraham.hardercore.util.PacketHandler;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("hardercore")
public class HarderCore {
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "hardercore";

    public HarderCore() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::setup);

        ItemInit.ITEMS.register(modEventBus);
        BlockInit.BLOCKS.register(modEventBus);
        EntityInit.ENTITY_TYPES.register(modEventBus);
        EffectInit.EFFECTS.register(modEventBus);
        TileEntityInit.TILE_ENTITY_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(EntityInit.SHADOW_KILLER.get(), ShadowKillerEntity.setCustomAttributes().create());
            GlobalEntityTypeAttributes.put(EntityInit.WONDERING_SPIRIT.get(), WonderingSpiritEntity.setCustomAttributes().create());
        });

        CapabilityManager.INSTANCE.register(IHarshEnvironment.class, new HarshEnvironmentStorage(), HarshEnvironment::new);
        PacketHandler.registerMessages(MOD_ID);
    }
}
