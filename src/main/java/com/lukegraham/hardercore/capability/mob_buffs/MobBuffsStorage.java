package com.lukegraham.hardercore.capability.mob_buffs;

import com.lukegraham.hardercore.capability.mob_buffs.IMobBuffs;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class MobBuffsStorage implements Capability.IStorage<IMobBuffs> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IMobBuffs> capability, IMobBuffs instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IMobBuffs> capability, IMobBuffs instance, Direction side, INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;
        instance.deserializeNBT(compound);
    }
}