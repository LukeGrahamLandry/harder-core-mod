package com.lukegraham.hardercore.client.model;

import com.lukegraham.hardercore.entities.WonderingSpiritEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.entity.monster.MonsterEntity;

public class WonderingSpiritModel extends BipedModel<WonderingSpiritEntity> {
    public WonderingSpiritModel(float modelSize, boolean p_i1168_2_) {
        this(modelSize, 0.0F, 64, p_i1168_2_ ? 32 : 64);
    }

    protected WonderingSpiritModel(float modelSize, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
        super(modelSize, yOffsetIn, textureWidthIn, textureHeightIn);
    }

    /**
     * Sets this entity's model rotation angles
     */
    public void setRotationAngles(WonderingSpiritEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ModelHelper.func_239105_a_(this.bipedLeftArm, this.bipedRightArm, this.isAggressive(entityIn), this.swingProgress, ageInTicks);
    }

    public boolean isAggressive(WonderingSpiritEntity entityIn) {
        return entityIn.isAggressive();
    }
}