package com.lukegraham.hardercore.effects;


import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.util.UUID;

public class HungryEffect extends Effect {
    public HungryEffect() {
        super(EffectType.HARMFUL, 0xFFFFFF);
        this.addAttributesModifier(Attributes.MOVEMENT_SPEED, UUID.randomUUID().toString(), -0.02F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributesModifier(Attributes.ATTACK_DAMAGE, UUID.randomUUID().toString(), -1.5F, AttributeModifier.Operation.ADDITION);
    }
}
