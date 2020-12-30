package com.lukegraham.hardercore.world_data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class HarderCoreData extends WorldSavedData {
    static String ID = "hardercore:worlddata";
    // the number of nights that has passed in the world
    // incremented by a successful sleep or night naturally turning to day
    // (or by setting the time to night during the day)
    int sleeps;
    int withers;  // the number of withers killed
    public HarderCoreData(){
        super(ID);
        this.sleeps = 0;
        this.withers = 0;
    }

    public static HarderCoreData getInstance(World world){
        return ((ServerWorld) world).getSavedData().getOrCreate(HarderCoreData::new, ID);
    }

    public int getSleeps() {
        return sleeps;
    }

    public int getWithers() {
        return withers;
    }

    public void increaseWithers(){
        this.withers += 1;
        this.markDirty();
    }

    public void increaseSleeps(){
        this.sleeps += 1;
        this.markDirty();
    }

    @Override
    public void read(CompoundNBT nbt) {
        this.sleeps = nbt.getInt("sleeps");
        this.withers = nbt.getInt("withers");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("sleeps", sleeps);
        compound.putInt("withers", withers);
        return compound;
    }
}
