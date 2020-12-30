package com.lukegraham.hardercore.client;

import com.lukegraham.hardercore.client.renderer.ShadowKillerRenderer;
import com.lukegraham.hardercore.client.renderer.WonderingSpiritRenderer;
import com.lukegraham.hardercore.init.EntityInit;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupHandler {
    @SubscribeEvent
    public static void bindEntityTextures(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.SHADOW_KILLER.get(), ShadowKillerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.WONDERING_SPIRIT.get(), WonderingSpiritRenderer::new);
    }
}
