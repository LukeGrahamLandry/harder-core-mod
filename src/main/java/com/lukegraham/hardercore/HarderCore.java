package com.lukegraham.hardercore;

import com.lukegraham.hardercore.capability.temp.ITemp;
import com.lukegraham.hardercore.capability.temp.Temp;
import com.lukegraham.hardercore.capability.temp.TempStorage;
import com.lukegraham.hardercore.entities.ShadowKillerEntity;
import com.lukegraham.hardercore.init.BlockInit;
import com.lukegraham.hardercore.init.EffectInit;
import com.lukegraham.hardercore.init.EntityInit;
import com.lukegraham.hardercore.init.ItemInit;
import com.lukegraham.hardercore.util.PacketHandler;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file and the MOD_ID constant below
@Mod("hardercore")
public class HarderCore
{
    // Let's you print to the console with FirstMod.LOGGER.debug(String)
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "hardercore";

    public HarderCore() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);

        ItemInit.ITEMS.register(modEventBus);    // registers your items
        BlockInit.BLOCKS.register(modEventBus);  // registers your blocks
        EntityInit.ENTITY_TYPES.register(modEventBus);
        EffectInit.EFFECTS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(EntityInit.SHADOW_KILLER.get(), ShadowKillerEntity.setCustomAttributes().create());

        });

        CapabilityManager.INSTANCE.register(ITemp.class, new TempStorage(), Temp::new);
        PacketHandler.registerMessages(MOD_ID);

    }

    private void doClientStuff(final FMLClientSetupEvent event) {}

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {}

}
