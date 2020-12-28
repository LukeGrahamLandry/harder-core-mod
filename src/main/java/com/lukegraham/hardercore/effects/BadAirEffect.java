package com.lukegraham.hardercore.effects;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;

import java.util.Random;
import java.util.UUID;

public class BadAirEffect extends Effect {
    Random rand = new Random();
    public BadAirEffect() {
        super(EffectType.HARMFUL, 0xFFFFFF);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return rand.nextInt(100) == 0;
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        if (amplifier > 1 && entity.getHealth() > 6.0F) entity.attackEntityFrom(DamageSource.DROWN, amplifier);
        else ((PlayerEntity)entity).getFoodStats().setFoodLevel(((PlayerEntity)entity).getFoodStats().getFoodLevel() - 2);

        if (rand.nextInt(5) == 0)
        entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 50 + rand.nextInt(20 + 100 * amplifier), 2, true, false));
    }
}
