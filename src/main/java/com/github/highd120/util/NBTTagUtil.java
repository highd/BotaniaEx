package com.github.highd120.util;

import java.util.Optional;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTTagUtil {
	public static NBTTagCompound getCompound(String key, ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) return new NBTTagCompound();
        if (!compound.hasKey(key)) return new NBTTagCompound();
        return compound.getCompoundTag(key);
	}

	public static Optional<String> getString(String key, ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) return Optional.empty();
        if (!compound.hasKey(key)) return Optional.empty();
		return Optional.ofNullable(compound.getString(key));
	}

	public static void setCompound(String key, ItemStack stack, NBTTagCompound tag) {
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null) {
        	stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setTag(key, tag);
	}

	public static void removeTag(String key, ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null) {
        	compound.removeTag(key);
        }
	}

	public static Optional<ItemStack> getInnerItem(String key, ItemStack stack) {
		NBTTagCompound compound = getCompound(key, stack);
		ItemStack inner = ItemStack.loadItemStackFromNBT(compound);
		return Optional.ofNullable(inner);
	}

	public static void setInnerItem(String key, ItemStack outer, ItemStack inner) {
		NBTTagCompound child = new NBTTagCompound();
		inner.writeToNBT(child);
		setCompound(key, outer, child);
	}
}
