package com.lukegraham.hardercore.client.renderer;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.client.model.WonderingSpiritModel;
import com.lukegraham.hardercore.entities.WonderingSpiritEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;

public class WonderingSpiritRenderer extends BipedRenderer<WonderingSpiritEntity, WonderingSpiritModel> {
    private static final ResourceLocation field_217771_a = new ResourceLocation(HarderCore.MOD_ID, "textures/entity/wondering_spirit.png");


    public WonderingSpiritRenderer(EntityRendererManager renderManagerIn) {
        this(renderManagerIn, new WonderingSpiritModel(0.0F, false), new WonderingSpiritModel(0.5F, true), new WonderingSpiritModel(1.0F, true));
    }

    protected WonderingSpiritRenderer(EntityRendererManager p_i50974_1_, WonderingSpiritModel p_i50974_2_, WonderingSpiritModel p_i50974_3_, WonderingSpiritModel p_i50974_4_) {
        super(p_i50974_1_, p_i50974_2_, 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, p_i50974_3_, p_i50974_4_));
    }

    @Override
    public void render(WonderingSpiritEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(WonderingSpiritEntity entity) {
        return field_217771_a;
    }
}
