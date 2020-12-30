package com.lukegraham.hardercore.blocks;

import com.lukegraham.hardercore.init.TileEntityInit;
import com.lukegraham.hardercore.tile_entity.GraveTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class GraveBlock extends Block {
    public GraveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote()){
            GraveTileEntity tile = (GraveTileEntity) worldIn.getTileEntity(pos);
            NonNullList<ItemStack> stacks = tile.getStacks();
            if (stacks.size() > 0) {
                ItemStack oldStack = player.inventory.offHandInventory.get(0);
                if (!stacks.get(0).isEmpty()){
                    player.inventory.offHandInventory.set(0, stacks.get(0));
                    giveItem(oldStack, player);
                }

                for (int i=0;i<4;i++){
                    if (stacks.get(i+1).isEmpty()) continue;
                    oldStack = player.inventory.armorInventory.get(i);
                    player.inventory.armorInventory.set(i, stacks.get(i+1));
                    giveItem(oldStack, player);
                }

                for (int i=0;i<36;i++){
                    if (stacks.get(i+5).isEmpty()) continue;
                    oldStack = player.inventory.mainInventory.get(i);
                    player.inventory.mainInventory.set(i, stacks.get(i+5));
                    giveItem(oldStack, player);
                }

                tile.setStacks(NonNullList.create());
            }

            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        }

        return ActionResultType.SUCCESS;

    }

    private void giveItem(ItemStack stack, PlayerEntity player){
        if (stack == ItemStack.EMPTY || stack == null) return;
        for(int i = 0; i < player.inventory.mainInventory.size(); ++i) {
            if (player.inventory.mainInventory.get(i).isEmpty()){
                player.inventory.mainInventory.set(i, stack);
                return;
            }
        }

        ItemEntity e = new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), stack);
        player.world.addEntity(e);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityInit.GRAVE.get().create();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(1, 0, 5, 15, 13, 11);
    }
}
