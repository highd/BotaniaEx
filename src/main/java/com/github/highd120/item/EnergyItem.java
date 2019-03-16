package com.github.highd120.item;

import java.util.List;

import com.github.highd120.ExtraWarpConfig;
import com.github.highd120.gui.GuiWarp;
import com.github.highd120.util.EnergyUtil;
import com.github.highd120.util.gui.GuiManager;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public abstract class EnergyItem extends Item implements IEnergyContainerItem{
	public EnergyItem() {
		setMaxStackSize(1);
        setNoRepair();
	}

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        subItems.add(new ItemStack(itemIn));
        ItemStack stack = new ItemStack(itemIn);
        EnergyUtil.setEnergy(stack, getMaxEnergyStored(stack));
        subItems.add(stack);
    }

	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        int energy = EnergyUtil.getEnergy(container);
        int energyReceived = Math.min(getMaxEnergyStored(container) - energy, Math.min(1000 * 10000, maxReceive));
        if (!simulate) {
            energy += energyReceived;
            if(energy < 0) energy = 0;
            EnergyUtil.setEnergy(container, energy);
        }
        return energyReceived;
	}

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        int energy = EnergyUtil.getEnergy(container);
        int energyExtracted = Math.min(energy, Math.min(10000, maxExtract));
        return energyExtracted;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        return EnergyUtil.getEnergy(container);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return !(getEnergyStored(stack) == getMaxEnergyStored(stack));
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1D - ((double)getEnergyStored(stack) / (double)getMaxEnergyStored(stack));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		IEnergyContainerItem item = (IEnergyContainerItem)stack.getItem();
		String text = String.format("%,d/%,d", item.getEnergyStored(stack), item.getMaxEnergyStored(stack));
        tooltip.add(text);
    }
}

