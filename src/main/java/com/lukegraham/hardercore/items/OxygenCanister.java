package com.lukegraham.hardercore.items;

import com.lukegraham.hardercore.capability.harsh_environment.HarshEnvironmentCapability;
import com.lukegraham.hardercore.events.AirQualityHandler;
import com.lukegraham.hardercore.util.KeyboardHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class OxygenCanister extends Item {
    boolean automatic;
    public OxygenCanister(Properties properties, boolean isAutomatic) {
        super(properties);
        this.automatic = isAutomatic;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (KeyboardHelper.isHoldingShift()) {
            if (automatic){
                tooltip.add(new StringTextComponent("Holds 100 air units and automatically uses them"));
            } else {
                tooltip.add(new StringTextComponent("Holds 20 air units"));
            }
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote() && entityIn instanceof PlayerEntity && random.nextInt(50) == 0){
            int localAirQuality = AirQualityHandler.calculateTotalQualityShift((PlayerEntity) entityIn);
            if (localAirQuality < 0){
                this.addAir(stack, -10 * localAirQuality);
            }

            if (this.automatic) {
                int missingAir = HarshEnvironmentCapability.getAirQuality((PlayerEntity) entityIn);
                if (missingAir > 0){
                    int amount = Math.min(getAir(stack), missingAir) * -1;
                    HarshEnvironmentCapability.addAirQuality((PlayerEntity) entityIn, amount);
                    addAir(stack, amount);
                }
            }
        }

        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return this.automatic;
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entity) {
        if (!worldIn.isRemote){
            if (entity.getAir() < entity.getMaxAir()){
                int missingAir = entity.getMaxAir() - entity.getAir();
                int amount = Math.min(getAir(stack) * 2, missingAir);
                entity.setAir(entity.getAir() + amount);
                addAir(stack, amount / -2);
            }

            if (getAir(stack) >= 10) {
                HarshEnvironmentCapability.addAirQuality((PlayerEntity) entity, -10);
                addAir(stack, -10);
            }
        }

        return stack;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack) {
        return 8;
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
        ItemStack stack = playerIn.getHeldItem(handIn);
        boolean needsAir = HarshEnvironmentCapability.getAirQuality(playerIn) >= 10 || playerIn.getAir() < playerIn.getMaxAir();
        if (getAir(stack) >= 10 && needsAir){
            playerIn.setActiveHand(handIn);
            return ActionResult.resultSuccess(stack);
        }

        return ActionResult.resultFail(stack);
    }

    public boolean showDurabilityBar(ItemStack stack){
        return getAir(stack) < (this.automatic ? 1000 : 200);
    }

    // @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 - getAir(stack) / (this.automatic ? 1000.0D : 200.0D);
    }

    private int getAir(ItemStack stack){
        if (!stack.hasTag()) return 0;
        CompoundNBT tag = stack.getTag();
        if (!tag.contains("air")) return 0;
        return tag.getInt("air");
    }

    private void addAir(ItemStack stack, int amount){
        CompoundNBT tag = stack.getTag();
        if (!stack.hasTag()) tag = new CompoundNBT();
        int air = 0;
        if (tag.contains("air")) air = tag.getInt("air");
        air += amount;
        air = Math.min(air, this.automatic ? 1000 : 200);
        air = Math.max(air, 0);
        tag.putInt("air", air);
        stack.setTag(tag);
    }
}
