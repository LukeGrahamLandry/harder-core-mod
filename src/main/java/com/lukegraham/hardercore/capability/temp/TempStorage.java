package com.lukegraham.hardercore.capability.temp;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TempStorage implements Capability.IStorage<ITemp> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<ITemp> capability, ITemp instance, Direction side) {
        CompoundNBT compound = new CompoundNBT();
        int temp = instance.getTemp();
        compound.putInt("temp", temp);
        return compound;
    }

    @Override
    public void readNBT(Capability<ITemp> capability, ITemp instance, Direction side, INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;
        int temp = compound.getInt("temp");
        instance.setTemp(temp);
    }
}