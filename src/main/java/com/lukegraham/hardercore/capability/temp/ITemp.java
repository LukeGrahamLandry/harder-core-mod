package com.lukegraham.hardercore.capability.temp;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ITemp extends INBTSerializable<CompoundNBT> {
    int getTemp();
    void addTemp(int amount);
    void setTemp(int amount);
}
