package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironmentCapProvider;
import com.lukegraham.hardercore.capability.mob_buffs.MobBuffs;
import com.lukegraham.hardercore.capability.mob_buffs.MobBuffsCapProvider;
import com.lukegraham.hardercore.capability.mob_buffs.MobBuffsCapability;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MobBuffHandler {
    private static final Random rand = new Random();

    @SubscribeEvent
    public static void buffMobs(LivingSpawnEvent.SpecialSpawn event){
        LivingEntity mob = event.getEntityLiving();
        if (mob.getEntityWorld().isRemote() || !mob.isNonBoss()) return;
        String title = "";
        boolean canBePoison = mob instanceof SkeletonEntity || mob instanceof SpiderEntity;
        if (canBePoison && rand.nextInt(20) == 0){
            MobBuffsCapability.addBuff(mob, "poison");
            title += "Poisonous ";
        }

        if (mob.isEntityUndead() && rand.nextInt(20) == 0){
            mob.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE));
            MobBuffsCapability.addBuff(mob, "fiery");
            title += "Fiery ";
        }

        if (rand.nextInt(20) == 0){
            MobBuffsCapability.addBuff(mob, "ninja");
            title += "Ninja ";
        }

        /* TODO: seems to not work
        if (rand.nextInt(2) == 0){
            MobBuffsCapability.addBuff(mob, "undying");
            title += "Undying ";
        }
         */

        if (!title.equals("")){
            String name = mob.getType().getName().getUnformattedComponentText();
            mob.setCustomName(new StringTextComponent(title + name));
            mob.setCustomNameVisible(true);
        }
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof LivingEntity && !(event.getObject() instanceof PlayerEntity)){
            ICapabilityProvider cap = new MobBuffsCapProvider();
            event.addCapability(new ResourceLocation(HarderCore.MOD_ID, "buffs"), cap);
        }
    }

    @SubscribeEvent
    public static void doPoison(LivingAttackEvent event){
        Entity source = event.getSource().getTrueSource();

        // WHEN THE MOB ATTACKS YOU
        if (source instanceof LivingEntity && !source.world.isRemote()){
            // gives you poison
            boolean isPoison = MobBuffsCapability.hasBuff((LivingEntity) source, "poison");
            if (isPoison){
                int level = MobBuffsCapability.getBuffLevel((LivingEntity) source, "poison");;
                int duration = 100 + rand.nextInt(1 + level * 100);
                event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.POISON, duration));
            }

            // lights you on fire
            boolean isFiery = MobBuffsCapability.hasBuff((LivingEntity) source, "fiery");
            if (isFiery){
                int level = MobBuffsCapability.getBuffLevel((LivingEntity) source, "fiery");;
                int duration = 100 + rand.nextInt(1 + level * 100);
                event.getEntityLiving().setFire(duration / 20);
            }
        }

        // WHEN YOU ATTACK MOB
        if (source instanceof PlayerEntity && !source.world.isRemote()){
            // dodges arrows like endermen
            boolean isNinja = MobBuffsCapability.hasBuff(event.getEntityLiving(), "ninja");
            if (isNinja){
                if (event.getSource() instanceof IndirectEntityDamageSource){
                    for(int i = 0; i < 64; ++i) {
                        if (teleportRandomly(event.getEntityLiving())) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void undying(LivingDeathEvent event){
        LivingEntity mob = event.getEntityLiving();
        // totem of undying
        boolean isUndying = MobBuffsCapability.hasBuff(mob, "undying");
        if (isUndying && !mob.world.isRemote()){
            HarderCore.LOGGER.debug("no death");
            event.setCanceled(true);
            MobBuffsCapability.removeBuff(mob, "undying");
            mob.addPotionEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
            mob.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
        }
    }

    protected static boolean teleportRandomly(LivingEntity mob) {
        if (!mob.world.isRemote() && mob.isAlive()) {
            double x = mob.getPosX() + (rand.nextDouble() - 0.5D) * 8.0D;
            double y = mob.getPosY() + (double)(rand.nextInt(4) - 2);
            double z = mob.getPosZ() + (rand.nextDouble() - 0.5D) * 8.0D;
            return mob.attemptTeleport(x, y, z, true);
        } else {
            return false;
        }
    }


}
