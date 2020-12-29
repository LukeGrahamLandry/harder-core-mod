package com.lukegraham.hardercore.capability.mob_buffs;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.capability.mob_buffs.IMobBuffs;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.Set;

public class MobBuffs implements IMobBuffs {
    HashMap<String, Integer> buffs;
    public MobBuffs() {
        this.buffs = new HashMap<>();
    }

    @Override
    public boolean hasBuff(String name){
        return this.buffs.containsKey(name);
    }

    @Override
    public int getBuffLevel(String name){
        return this.buffs.getOrDefault(name, -1);
    }

    @Override
    public void addBuff(String name, int level){
        this.buffs.put(name, level);
    }

    @Override
    public void removeBuff(String name){
        this.buffs.remove(name);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        this.buffs.forEach(nbt::putInt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        Set<String> keys = nbt.keySet();
        keys.forEach((name) -> {
            this.addBuff(name, nbt.getInt(name));
        });
    }
}
