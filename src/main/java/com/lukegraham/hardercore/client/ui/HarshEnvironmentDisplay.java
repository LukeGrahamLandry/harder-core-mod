package com.lukegraham.hardercore.client.ui;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironmentCapability;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HarshEnvironmentDisplay {
    public static void buildOverlay(MatrixStack matrices) {
        PlayerEntity player = Minecraft.getInstance().player;
        int temp = HarshEnvironmentCapability.getTemp(player);
        int color = getTempColour(temp);

        if (Math.abs(temp) > 15)
        Minecraft.getInstance().fontRenderer.drawString(matrices,Integer.toString(temp), 5, 5, color);

        int quality = HarshEnvironmentCapability.getAirQuality(player);
        color = getQualityColour(quality);
        if (quality > 5)
            Minecraft.getInstance().fontRenderer.drawString(matrices, (100 - quality) + "%", 5, 15, color);

        int thirst = HarshEnvironmentCapability.getThirst(player);
        color = getThirstColour(thirst);
        if (thirst > 5)
            Minecraft.getInstance().fontRenderer.drawString(matrices, (100 - thirst) + "%", 5, 25, color);
    }

    private static int getTempColour(int temp){
        int green = 255;
        int red = 255;
        int blue = 255;

        if (temp > 0){
            int shift = Math.min(255, temp);
            blue -= shift;
            green -= shift;
        }
        if (temp < 0){
            int shift = Math.min(255, temp*-1);
            red -= shift;
            green -= shift;
        }

        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;

        return rgb;
    }

    private static int getQualityColour(int quality){
        int green = (int) Math.floor(quality * 2.4D);
        int red = (int) Math.floor(quality * 2.4D);
        int blue = (int) Math.floor(quality * 2.4D);

        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;

        return rgb;
    }

    private static int getThirstColour(int thirst) {
        int green = (int) Math.floor(thirst * 2.4D);
        int red = (int) Math.floor(thirst * 2.4D);
        int blue = 255;

        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;

        return rgb;
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
