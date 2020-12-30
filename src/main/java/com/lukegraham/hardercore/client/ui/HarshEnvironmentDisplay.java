package com.lukegraham.hardercore.client.ui;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironmentCapability;
import com.mojang.blaze3d.matrix.MatrixStack;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HarshEnvironmentDisplay {
    private static final Drawable WATER_DROP = new Drawable("textures/gui/water_drop.png", 8, 8);
    private static final Drawable EMPTY_DROP = new Drawable("textures/gui/empty_drop.png", 8, 8);
    private static final Drawable GAS_MASK = new Drawable("textures/gui/bad_air.png", 8, 8);

    private static final Drawable MAX_COLD = new Drawable("textures/gui/temp/max_cold.png", 24, 24);
    private static final Drawable COLD = new Drawable("textures/gui/temp/cold.png", 24, 24);
    private static final Drawable MIN_COLD = new Drawable("textures/gui/temp/min_cold.png", 24, 24);
    private static final Drawable STABLE_TEMP = new Drawable("textures/gui/temp/stable.png", 24, 24);
    private static final Drawable MIN_HOT = new Drawable("textures/gui/temp/min_hot.png", 24, 24);
    private static final Drawable HOT = new Drawable("textures/gui/temp/hot.png", 24, 24);
    private static final Drawable MAX_HOT = new Drawable("textures/gui/temp/max_hot.png", 24, 24);

    private static void renderWaterBar(PlayerEntity player){
        int thirst = HarshEnvironmentCapability.getThirst(player);
        boolean underWater = player.getAir() < player.getMaxAir();

        int scaledWidth = Minecraft.getInstance().getMainWindow().getScaledWidth();
        int scaledHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();
        int j1 = scaledWidth / 2 + 91;
        int foodY = scaledHeight - 39;
        int y = foodY - 9;
        if (underWater) y -= 9;

        for (int posInRow = 0; posInRow < 10; posInRow++){
            int x = j1 - posInRow * 8 - 9;
            boolean shouldDraw = thirst < ((10 - posInRow) * 10);
            if (shouldDraw) WATER_DROP.draw(x, y);
            else EMPTY_DROP.draw(x, y);
        }
    }

    private static void renderAirBar(PlayerEntity player) {
        int quality = HarshEnvironmentCapability.getAirQuality(player);
        if (quality == 0) return;
        boolean underWater = player.getAir() < player.getMaxAir();

        int scaledWidth = Minecraft.getInstance().getMainWindow().getScaledWidth();
        int scaledHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();
        int j1 = scaledWidth / 2 + 91;
        int foodY = scaledHeight - 39;
        int y = foodY - 9 - 9;
        if (underWater) y -= 9;

        for (int posInRow = 0; posInRow < 10; posInRow++){
            int x = j1 - posInRow * 8 - 9;
            boolean shouldDraw = quality < ((10 - posInRow) * 10);
            if (shouldDraw) GAS_MASK.draw(x, y);
        }
    }

    private static void renderThermometer(PlayerEntity player) {
        int temp = HarshEnvironmentCapability.getTemp(player);

        Drawable texture;
        if (Math.abs(temp) < 20){
            texture = STABLE_TEMP;
        } else if (temp > 0 && temp < 100){
            texture = MIN_HOT;
        } else if (temp > 0 && temp < 200){
            texture = HOT;
        } else if (temp > 0 && temp <= 300){
            texture = MAX_HOT;
        }else if (temp < 0 && temp > -100){
            texture = MIN_COLD;
        } else if (temp < 0 && temp > -200){
            texture = COLD;
        } else if (temp < 0 && temp >= -300){
            texture = MAX_COLD;
        } else {
            throw new ValueException("INVALID TEMP: " + temp);
        }

        texture.draw(5, 5);
    }


    @SubscribeEvent
    public static void renderHarshEnvironmentUI(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            PlayerController controller = Minecraft.getInstance().playerController;
            PlayerEntity player = Minecraft.getInstance().player;
            if (controller == null || player == null ) {
                return;
            }

            if (!Minecraft.getInstance().gameSettings.showDebugInfo && controller.shouldDrawHUD()) {
                renderWaterBar(player);
                renderAirBar(player);
                renderThermometer(player);
            }
        }
    }
}

