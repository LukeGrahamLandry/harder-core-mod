package com.lukegraham.hardercore.capability.harsh_environment;

import net.minecraft.nbt.CompoundNBT;

public class HarshEnvironment implements IHarshEnvironment {
    int temp;
    int quality;
    int thirst;
    public HarshEnvironment() {
        this.temp = 0;
        this.quality = 0;
        this.thirst = 0;
    }

    @Override
    public int getTemp() {
        return temp;
    }

    @Override
    public void addTemp(int amount) {
        temp += amount;
        temp = Math.max(temp, -300);
        temp = Math.min(temp, 300);
    }

    @Override
    public void setTemp(int amount) {
        temp = amount;
        temp = Math.max(temp, -300);
        temp = Math.min(temp, 300);
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
    public int getThirst() {
        return thirst;
    }

    @Override
    public void addThirst(int amount) {
        thirst += amount;
        thirst = Math.max(thirst, 0);
        thirst = Math.min(thirst, 100);
    }

    @Override
    public void setThirst(int amount) {
        thirst = amount;
        thirst = Math.max(thirst, 0);
        thirst = Math.min(thirst, 100);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("temp", temp);
        nbt.putInt("quality", quality);
        nbt.putInt("thirst", thirst);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setTemp(nbt.getInt("temp"));
        setAirQuality(nbt.getInt("quality"));
        setThirst(nbt.getInt("thirst"));
    }
}
