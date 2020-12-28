package com.lukegraham.hardercore.capability.harsh_environment;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class HarshEnvironmentStorage implements Capability.IStorage<IHarshEnvironment> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IHarshEnvironment> capability, IHarshEnvironment instance, Direction side) {
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("temp", instance.getTemp());
        compound.putInt("quality", instance.getAirQuality());
        compound.putInt("thirst", instance.getThirst());
        return compound;
    }

    @Override
    public void readNBT(Capability<IHarshEnvironment> capability, IHarshEnvironment instance, Direction side, INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;
        instance.setTemp(compound.getInt("temp"));
        instance.setAirQuality(compound.getInt("quality"));
        instance.setThirst(compound.getInt("thirst"));
    }
}