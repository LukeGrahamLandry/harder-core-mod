package com.lukegraham.hardercore.events;

import com.lukegraham.hardercore.config.HarderCoreConfig;
import com.lukegraham.hardercore.entities.WonderingSpiritEntity;
import com.lukegraham.hardercore.init.BlockInit;
import com.lukegraham.hardercore.init.EntityInit;
import com.lukegraham.hardercore.tile_entity.GraveTileEntity;
import net.minecraft.block.TallGrassBlock;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GraveHandler {
    @SubscribeEvent
    public static void makeGraveOnDeath(LivingDeathEvent event){
        if (HarderCoreConfig.doGraves.get() &&
                event.getEntity() instanceof PlayerEntity && !event.getEntity().getEntityWorld().isRemote()){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();

            BlockPos pos = calculateGravePosition(player);
            doGrave(player, pos);
            spawnGhost(player.getEntityWorld(), pos);
        }
    }

    private static BlockPos calculateGravePosition(PlayerEntity player) {
        BlockPos pos =  player.getPosition();
        if (pos.getY() < 0){  // dont spawn grave in the void if you fell down the end
            pos = new BlockPos(pos.getX(), 1, pos.getZ());
        }

        // dont spawn grave in a block
        for (int i=0;i<32;i++){
            boolean valid = player.getEntityWorld().getBlockState(pos.up(i)).isAir() ||
                    player.getEntityWorld().getBlockState(pos.up(i)).getBlock() instanceof TallGrassBlock ||
                    !player.getEntityWorld().getFluidState(pos.up(i)).isEmpty();
            if (valid){
                pos = pos.up(i);
                break;
            }
        }
        return pos;
    }

    private static void doGrave(PlayerEntity player, BlockPos pos){
        NonNullList<ItemStack> stacks = NonNullList.create();
        stacks.addAll(player.inventory.offHandInventory);
        stacks.addAll(player.inventory.armorInventory);
        stacks.addAll(player.inventory.mainInventory);
        player.inventory.mainInventory.clear();
        player.inventory.armorInventory.clear();
        player.inventory.offHandInventory.clear();

        boolean hasItems = false;
        for (ItemStack stack : stacks){
            if (!stack.isEmpty()){
                hasItems = true;
                break;
            }
        }

        if (hasItems){
            player.getEntityWorld().setBlockState(pos, BlockInit.GRAVE.get().getDefaultState());
            GraveTileEntity tile = (GraveTileEntity) player.getEntityWorld().getTileEntity(pos);
            tile.setStacks(stacks);

            player.sendMessage(new StringTextComponent("Your grave is at (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")"), player.getUniqueID());
        }
    }

    private static void spawnGhost(World world, BlockPos pos) {
        MobEntity mob = new WonderingSpiritEntity(EntityInit.WONDERING_SPIRIT.get(), world);
        mob.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
        mob.enablePersistence();
        world.addEntity(mob);
    }
}
