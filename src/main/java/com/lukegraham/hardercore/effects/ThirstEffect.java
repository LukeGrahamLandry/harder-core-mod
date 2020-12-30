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

public class ThirstEffect extends Effect {
    DamageSource source = (new DamageSource("thirst")).setDamageBypassesArmor().setDamageIsAbsolute();
    Random rand = new Random();

    public ThirstEffect() {
        super(EffectType.HARMFUL, 0xFFFFFF);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return rand.nextInt(60) == 0;
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        if (amplifier > 0 && (entity.getHealth() > 5.0F || amplifier >= 3)) entity.attackEntityFrom(source, amplifier);

        if (rand.nextInt(amplifier > 1 ? 3 : 8) == 0)
        entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 100 + rand.nextInt(20 + 100 * amplifier), amplifier, true, false));
    }
}
