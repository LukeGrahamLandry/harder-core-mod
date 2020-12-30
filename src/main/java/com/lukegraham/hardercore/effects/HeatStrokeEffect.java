package com.lukegraham.hardercore.effects;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;

import java.util.Random;
import java.util.UUID;

public class HeatStrokeEffect extends Effect {
    Random rand = new Random();
    public HeatStrokeEffect() {
        super(EffectType.HARMFUL, 0xFFFFFF);
        this.addAttributesModifier(Attributes.ATTACK_DAMAGE, UUID.randomUUID().toString(), -1.0F, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return rand.nextInt(600) == 0;
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        if (amplifier > 0) entity.attackEntityFrom(DamageSource.ON_FIRE, 2.0F);
        if (amplifier == 2) {
            entity.setFire(200);
            entity.attackEntityFrom(DamageSource.ON_FIRE, 10.0F);
        }
        entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 150 + rand.nextInt(20 + 100 * amplifier), 1, true, false));
    }
}
