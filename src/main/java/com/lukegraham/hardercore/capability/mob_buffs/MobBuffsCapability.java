package com.lukegraham.hardercore.capability.mob_buffs;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.capability.harsh_environment.IHarshEnvironment;
import com.lukegraham.hardercore.util.PacketHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MobBuffsCapability {
    @CapabilityInject(IMobBuffs.class)
    public static Capability<IMobBuffs> BUFF_CAPABILITY;

    public static final Direction DEFAULT_FACING = null;

    public static LazyOptional<IMobBuffs> getMobBuffCap(final LivingEntity mob) {
        LazyOptional<IMobBuffs> lazyCap = mob.getCapability(BUFF_CAPABILITY, DEFAULT_FACING);
        // IMobBuffs cap = lazyCap.isPresent() ? lazyCap.getValue() : null;
        return lazyCap;
    }

    public static void addBuff(final LivingEntity mob, String name){
        addBuff(mob, name, 0);
    }

    public static void addBuff(final LivingEntity mob, String name, int level){
        getMobBuffCap(mob).ifPresent(cap -> cap.addBuff(name, level));
        sendUpdatePacket(mob);
    }

    public static boolean hasBuff(final LivingEntity mob, String name) {
        if (mob == null) return false;

        AtomicBoolean flag = new AtomicBoolean(false);
        getMobBuffCap(mob).ifPresent((cap) -> {
            flag.set(cap.hasBuff(name));
        });
        return flag.get();
    }

    public static int getBuffLevel(final LivingEntity mob, String name) {
        if (mob == null) return -1;

        AtomicInteger flag = new AtomicInteger(-1);
        getMobBuffCap(mob).ifPresent((cap) -> {
            flag.set(cap.getBuffLevel(name));
        });
        return flag.get();
    }

    public static void removeBuff(LivingEntity mob, String name) {
        getMobBuffCap(mob).ifPresent(cap -> cap.removeBuff(name));
        sendUpdatePacket(mob);
    }

    public static void sendUpdatePacket(LivingEntity mob){
        // probably not actually needed on the client
    }


}
