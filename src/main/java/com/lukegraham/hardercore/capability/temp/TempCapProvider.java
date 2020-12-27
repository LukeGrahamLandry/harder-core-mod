package com.lukegraham.hardercore.capability.temp;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TempCapProvider implements ICapabilitySerializable<CompoundNBT>
{
    private final Temp temp;

    public TempCapProvider()
    {
        temp = new Temp();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        return cap == TempCapability.TEMP_CAPABILITY ? LazyOptional.of(() -> temp).cast() : LazyOptional.empty();
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
