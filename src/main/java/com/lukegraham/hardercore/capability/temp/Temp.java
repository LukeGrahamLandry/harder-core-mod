package com.lukegraham.hardercore.capability.temp;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class Temp implements ITemp {
    int temp;
    int quality;
    public Temp() {
        this.temp = 0;
        this.quality = 0;
    }

    @Override
    public int getTemp() {
        return temp;
    }

    @Override
    public void addTemp(int amount) {
        temp += amount;
        temp = Math.max(temp, -255);
        temp = Math.min(temp, 255);
    }

    @Override
    public void setTemp(int amount) {
        temp = amount;
        temp = Math.max(temp, -255);
        temp = Math.min(temp, 255);
    }

    @Override
    public int getAirQuality() {
        return quality;
    }

    @Override
    public void addAirQuality(int amount) {
        quality += amount;
        quality = Math.max(quality, 0);
        quality = Math.min(quality, 100);
    }

    @Override
    public void setAirQuality(int amount) {
        quality = amount;
        quality = Math.max(quality, 0);
        quality = Math.min(quality, 100);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("temp", temp);
        nbt.putInt("quality", quality);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setTemp(nbt.getInt("temp"));
        setAirQuality(nbt.getInt("quality"));
    }
}
