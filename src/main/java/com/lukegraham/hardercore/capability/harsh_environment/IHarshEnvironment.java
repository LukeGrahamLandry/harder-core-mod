package com.lukegraham.hardercore.capability.harsh_environment;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IHarshEnvironment extends INBTSerializable<CompoundNBT> {
    // -255 -> 255; 0 is good, lower is cold, higher is hot
    int getTemp();
    void addTemp(int amount);
    void setTemp(int amount);

    // 0 -> 100; Low is better
    int getAirQuality();
    void addAirQuality(int amount);
    void setAirQuality(int amount);

    int getThirst();
    void addThirst(int amount);
    void setThirst(int amount);
}
