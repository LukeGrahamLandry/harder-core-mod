package com.lukegraham.hardercore.capability.temp;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class Temp implements ITemp {
    int temp;
    public Temp() {
        this.temp = 0;
    }

    @Override
    public int getTemp() {
        return temp;
    }

    @Override
    public void addTemp(int amount) {
        temp += amount;
    }

    @Override
    public void setTemp(int amount) {
        temp = amount;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("temp", temp);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setTemp(nbt.getInt("temp"));
    }
}
