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
    DamageSource source = (new DamageSource("bad_air")).setDamageBypassesArmor().setDamageIsAbsolute();
    Random rand = new Random();

    public BadAirEffect() {
        super(EffectType.HARMFUL, 0xFFFFFF);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return rand.nextInt(200) == 0;
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        entity.attackEntityFrom(source, amplifier + 1);
        if (amplifier >= 3) entity.attackEntityFrom(source, 10);

        if (rand.nextInt(2) == 0 && amplifier >= 1)
            entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 50 + rand.nextInt(20 + 100 * amplifier), 1, true, false));

        if (amplifier >= 2)
            entity.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100, 0, true, false));
    }
}
