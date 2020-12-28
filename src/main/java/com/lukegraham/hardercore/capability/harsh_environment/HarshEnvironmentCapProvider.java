package com.lukegraham.hardercore.capability.harsh_environment;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class HarshEnvironmentCapProvider implements ICapabilitySerializable<CompoundNBT>
{
    private final HarshEnvironment temp;

    public HarshEnvironmentCapProvider()
    {
        temp = new HarshEnvironment();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        return cap == HarshEnvironmentCapability.TEMP_CAPABILITY ? LazyOptional.of(() -> temp).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT compound = new CompoundNBT();
        compound.put("temp", temp.serializeNBT());
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        temp.deserializeNBT(nbt.getCompound("temp"));
    }
}
