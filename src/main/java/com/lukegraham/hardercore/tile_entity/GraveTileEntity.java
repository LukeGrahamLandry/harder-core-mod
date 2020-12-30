package com.lukegraham.hardercore.tile_entity;

import com.lukegraham.hardercore.HarderCore;
import com.lukegraham.hardercore.init.TileEntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class GraveTileEntity extends TileEntity {
    NonNullList<ItemStack> stacks;
    public GraveTileEntity() {
        super(TileEntityInit.GRAVE.get());
        this.stacks = NonNullList.create();
    }


    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);

        int i = 0;
        while (nbt.contains(String.valueOf(i))) {
            CompoundNBT tag = nbt.getCompound(String.valueOf(i));
            ItemStack stack = ItemStack.read(tag);
            this.stacks.add(stack);
            i++;
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);

        for (int i=0;i<this.stacks.size();i++){
            ItemStack stack = this.stacks.get(i);
            CompoundNBT tag = stack.write(new CompoundNBT());
            nbt.put(String.valueOf(i), tag);
        }

        return nbt;
    }

    public void setStacks(NonNullList<ItemStack> newStacks){
        this.stacks = NonNullList.create();
        this.stacks.addAll(newStacks);
        this.markDirty();
    }

    public NonNullList<ItemStack> getStacks(){
        return this.stacks;
    }

    @Override
    public void remove() {
        super.remove();
        for (ItemStack stack : this.stacks){
            if (stack != ItemStack.EMPTY){
                ItemEntity e = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                this.world.addEntity(e);
            }
        }
    }
}
