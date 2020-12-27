package com.lukegraham.hardercore.client.ui;

import com.lukegraham.hardercore.capability.temp.TempCapability;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TempDisplay {
    public static void buildOverlay(MatrixStack matrices) {
        PlayerEntity player = Minecraft.getInstance().player;
        int coins = TempCapability.getTemp(player);


        Minecraft.getInstance().fontRenderer.drawString(matrices,Integer.toString(coins), 29, 10 + 4, 16767796);

    }

    @SubscribeEvent
    public static void renderTempText(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            PlayerController controller = Minecraft.getInstance().playerController;
            if (controller == null) {
                return;
            }
            PlayerEntity player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }

            if (!Minecraft.getInstance().gameSettings.showDebugInfo) {
                buildOverlay(event.getMatrixStack());
            }
        }
    }
}
