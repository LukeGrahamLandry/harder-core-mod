package com.lukegraham.hardercore.effects;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

import java.util.Random;
import java.util.UUID;

public class HypothermiaEffect extends Effect {
    Random rand = new Random();
    public HypothermiaEffect() {
        super(EffectType.HARMFUL, 0xFFFFFF);
        this.addAttributesModifier(Attributes.MOVEMENT_SPEED, UUID.randomUUID().toString(), -0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributesModifier(Attributes.ATTACK_SPEED, UUID.randomUUID().toString(), -0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributesModifier(Attributes.ATTACK_DAMAGE, UUID.randomUUID().toString(), -1.0F, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return amplifier > 3 && rand.nextInt(200) == 0;
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        entity.attackEntityFrom(DamageSource.MAGIC, 1.0F);
    }
}
