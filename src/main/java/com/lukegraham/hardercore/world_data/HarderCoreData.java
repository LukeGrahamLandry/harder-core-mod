package com.lukegraham.hardercore.world_data;

import com.lukegraham.hardercore.HarderCore;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;

public class HarderCoreData extends WorldSavedData {
    static String ID = "hardercore:worlddata";
    // the number of nights that has passed in the world
    // incremented by a successful sleep or night naturally turning to day
    // (or by setting the time to night during the day)
    int sleeps;
    int withers;  // the number of withers killed
    ArrayList<String> playersGivenBook;
    public HarderCoreData(){
        super(ID);
        this.sleeps = 0;
        this.withers = 0;
        playersGivenBook = new ArrayList();
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

    public boolean hasPlayerGottenBook(PlayerEntity player){
        String name = player.getName().getString();
        HarderCore.LOGGER.debug(name);
        return this.playersGivenBook.contains(name);
    }

    public void setPlayerHasGotBook(PlayerEntity player){
        String name = player.getName().getString();
        this.playersGivenBook.add(name);
        this.markDirty();
    }

    @Override
    public void read(CompoundNBT nbt) {
        this.sleeps = nbt.getInt("sleeps");
        this.withers = nbt.getInt("withers");

        CompoundNBT bookTag = nbt.getCompound("playersgivenbook");
        int i = 0;
        while (bookTag.contains(String.valueOf(i))){
            String name = bookTag.getString(String.valueOf(i));
            this.playersGivenBook.add(name);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("sleeps", sleeps);
        compound.putInt("withers", withers);

        CompoundNBT bookTag = new CompoundNBT();
        for (int i=0;i<playersGivenBook.size();i++){
            bookTag.putString(String.valueOf(i), playersGivenBook.get(i));
        }
        compound.put("playersgivenbook", bookTag);

        return compound;
    }
}
