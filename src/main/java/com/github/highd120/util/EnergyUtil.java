package com.github.highd120.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class EnergyUtil {
	private static final String ENERGY_NBT_KEY = "Energy";

	public static int getEnergy(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) return 0;
        if (!compound.hasKey(ENERGY_NBT_KEY)) return 0;
        return stack.getTagCompound().getInteger(ENERGY_NBT_KEY);
	}

	public static void setEnergy(ItemStack stack, int energy) {
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null) {
        	stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("Energy", energy);;
	}
}
