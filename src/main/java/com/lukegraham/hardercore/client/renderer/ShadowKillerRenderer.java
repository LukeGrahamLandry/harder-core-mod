package com.lukegraham.hardercore.client.renderer;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.entities.ShadowKillerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SilverfishModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ShadowKillerRenderer extends MobRenderer<MobEntity, SilverfishModel<MobEntity>> {
    private static final ResourceLocation SILVERFISH_TEXTURES = new ResourceLocation(HarderCore.MOD_ID, "textures/entity/shadow_killer.png");

    public ShadowKillerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SilverfishModel<>(), 0.3F);
    }

    protected float getDeathMaxRotation(MobEntity entityLivingBaseIn) {
        return 180.0F;
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(MobEntity entity) {
        return SILVERFISH_TEXTURES;
    }
}
