package com.lukegraham.hardercore.capability.mob_buffs;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IMobBuffs extends INBTSerializable<CompoundNBT> {
    boolean hasBuff(String name);
    int getBuffLevel(String name);
    void addBuff(String name, int level);
    void removeBuff(String name);
}
