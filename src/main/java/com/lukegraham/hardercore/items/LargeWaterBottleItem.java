package com.lukegraham.hardercore.items;

import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironmentCapability;
import com.lukegraham.hardercore.util.KeyboardHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class LargeWaterBottleItem extends Item {
    boolean automatic;
    public LargeWaterBottleItem(Properties properties, boolean isAutomatic) {
        super(properties);
        this.automatic = isAutomatic;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (KeyboardHelper.isHoldingShift()) {
            int water = getWater(stack);
            if (automatic){
                tooltip.add(new StringTextComponent("Holds 100 water units and automatically drinks for you"));
            } else {
                tooltip.add(new StringTextComponent("Holds 20 water units"));
            }

        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (this.automatic && !worldIn.isRemote() && random.nextInt(50) == 0) {
            int missingWater = HarshEnvironmentCapability.getThirst((PlayerEntity) entityIn);
            if (missingWater > 0){
                int amount = Math.min(getWater(stack), missingWater) * -1;
                HarshEnvironmentCapability.addThirst((PlayerEntity) entityIn, amount);
                addWater(stack, amount);
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return this.automatic;
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entity) {
        if (!worldIn.isRemote && getWater(stack) >= 10) {
            HarshEnvironmentCapability.addThirst((PlayerEntity) entity, -10);
            addWater(stack, -10);
        }

        return stack;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     * {@link #onItemUse}.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        BlockRayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
        if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = raytraceresult.getPos();
            if (worldIn.getFluidState(blockpos).isTagged(FluidTags.WATER)) {
                worldIn.playSound(playerIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
                addWater(itemstack, 100);
                return ActionResult.resultSuccess(itemstack);
            }
        }

        boolean isThirsty = HarshEnvironmentCapability.getThirst(playerIn) >= 10;
        if (getWater(itemstack) >= 10 && isThirsty){
            playerIn.setActiveHand(handIn);
            return ActionResult.resultSuccess(itemstack);
        }

        return ActionResult.resultFail(itemstack);
    }

    public boolean showDurabilityBar(ItemStack stack){
        return getWater(stack) < (this.automatic ? 1000 : 200);
    }

    // @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 - getWater(stack) / (this.automatic ? 1000.0D : 200.0D);
    }

    private int getWater(ItemStack stack){
        if (!stack.hasTag()) return 0;
        CompoundNBT tag = stack.getTag();
        if (!tag.contains("water")) return 0;
        return tag.getInt("water");
    }

    private void addWater(ItemStack stack, int amount){
        CompoundNBT tag = stack.getTag();
        if (!stack.hasTag()) tag = new CompoundNBT();
        int water = 0;
        if (tag.contains("water")) water = tag.getInt("water");
        water += amount;
        water = Math.min(water, this.automatic ? 1000 : 200);
        water = Math.max(water, 0);
        tag.putInt("water", water);
        stack.setTag(tag);
    }
}
