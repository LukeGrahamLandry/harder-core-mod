package com.lukegraham.hardercore.client.renderer;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.entities.ShadowKillerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SilverfishModel;
import net.minecraft.util.ResourceLocation;

public class ShadowKillerRenderer extends MobRenderer<ShadowKillerEntity, SilverfishModel<ShadowKillerEntity>> {
    private static final ResourceLocation SILVERFISH_TEXTURES = new ResourceLocation(HarderCore.MOD_ID, "textures/entity/shadow_killer.png");

    public ShadowKillerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SilverfishModel<>(), 0.3F);
    }

    protected float getDeathMaxRotation(ShadowKillerEntity entityLivingBaseIn) {
        return 180.0F;
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(ShadowKillerEntity entity) {
        return SILVERFISH_TEXTURES;
    }
}
