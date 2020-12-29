package com.lukegraham.hardercore.capability.mob_buffs;

import com.lukegraham.hardercore.capability.mob_buffs.MobBuffs;
import com.lukegraham.hardercore.capability.mob_buffs.MobBuffsCapability;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class MobBuffsCapProvider implements ICapabilitySerializable<CompoundNBT>
{
    private final MobBuffs buffs;

    public MobBuffsCapProvider()
    {
        buffs = new MobBuffs();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        return cap == MobBuffsCapability.BUFF_CAPABILITY ? LazyOptional.of(() -> buffs).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT compound = new CompoundNBT();
        compound.put("buffs", buffs.serializeNBT());
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        buffs.deserializeNBT(nbt.getCompound("buffs"));
    }
}
