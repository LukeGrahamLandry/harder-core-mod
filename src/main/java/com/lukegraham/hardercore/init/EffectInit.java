package com.lukegraham.hardercore.init;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.effects.ExhaustionEffect;
import com.lukegraham.hardercore.effects.HeatStrokeEffect;
import com.lukegraham.hardercore.effects.HypothermiaEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectInit {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, HarderCore.MOD_ID);

    public static final RegistryObject<Effect> EXHAUSTION = EFFECTS.register("exhaustion", ExhaustionEffect::new);
    public static final RegistryObject<Effect> HUNGRY = EFFECTS.register("hungry", ExhaustionEffect::new);
    public static final RegistryObject<Effect> HEAT_STROKE = EFFECTS.register("heat_stroke", HeatStrokeEffect::new);
    public static final RegistryObject<Effect> HYPOTHERMIA = EFFECTS.register("hypothermia", HypothermiaEffect::new);
}