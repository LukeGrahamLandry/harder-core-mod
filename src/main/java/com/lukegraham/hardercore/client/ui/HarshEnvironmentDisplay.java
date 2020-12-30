package com.lukegraham.hardercore.client.ui;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironmentCapability;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HarshEnvironmentDisplay {
    private static final Drawable WATER_DROP = new Drawable("textures/items/anti_shadow_charm.png", 8, 8);

    public static void buildOverlay(MatrixStack matrices) {
        PlayerEntity player = Minecraft.getInstance().player;

        int thirst = HarshEnvironmentCapability.getThirst(player);
        renderWaterBar(thirst);

        int temp = HarshEnvironmentCapability.getTemp(player);
        int color = getTempColour(temp);
        if (Math.abs(temp) > 15)
        Minecraft.getInstance().fontRenderer.drawString(matrices,Integer.toString(temp), 5, 5, color);

        int quality = HarshEnvironmentCapability.getAirQuality(player);
        color = getQualityColour(quality);
        if (quality > 5)
            Minecraft.getInstance().fontRenderer.drawString(matrices, (100 - quality) + "%", 5, 15, color);


        // color = getThirstColour(thirst);
        // if (thirst > 5) Minecraft.getInstance().fontRenderer.drawString(matrices, (100 - thirst) + "%", 5, 25, color);


    }

    private static void renderWaterBar(int thirst){
        int scaledWidth = Minecraft.getInstance().getMainWindow().getScaledWidth();
        int scaledHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();
        int j1 = scaledWidth / 2 + 91;
        int foodY = scaledHeight - 39;
        int y = foodY - 9;

        for (int posInRow = 0; posInRow < 10; posInRow++){
            int x = j1 - posInRow * 8 - 9;
            boolean shouldDraw = thirst < ((10 - posInRow) * 10);
            if (shouldDraw) WATER_DROP.draw(x, y);
        }
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
            PlayerEntity player = Minecraft.getInstance().player;
            if (controller == null || player == null ) {
                return;
            }

            if (!Minecraft.getInstance().gameSettings.showDebugInfo && controller.shouldDrawHUD()) {
                buildOverlay(event.getMatrixStack());
            }
        }
    }
}
