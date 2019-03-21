package com.github.highd120.util;

import java.util.Optional;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * NBTタグのユーティリティ。
 * @author hdgam
 */
public class NbtTagUtil {
    /**
     * Compoundの取得。
     * @param key キー。
     * @param stack アイテム。
     * @return Compound。
     */
    public static NBTTagCompound getCompound(String key, ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) {
            return new NBTTagCompound();
        }
        if (!compound.hasKey(key)) {
            return new NBTTagCompound();
        }
        return compound.getCompoundTag(key);
    }

    /**
     * NBTタグから文字列の取得。
     * @param key キー。
     * @param stack アイテム。
     * @return 文字列
     */
    public static Optional<String> getString(String key, ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) {
            return Optional.empty();
        }
        if (!compound.hasKey(key)) {
            return Optional.empty();
        }
        return Optional.ofNullable(compound.getString(key));
    }

    /**
     * Compoundのセット。
     * @param key キー。
     * @param stack アイテム。
     * @param tag Compound。
     */
    public static void setCompound(String key, ItemStack stack, NBTTagCompound tag) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setTag(key, tag);
    }

    /**
     * タグの削除。
     * @param key キー。
     * @param stack アイテム。
     */
    public static void removeTag(String key, ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null) {
            compound.removeTag(key);
        }
    }

    /**
     * 内部アイテムの取得。
     * @param key キー。
     * @param stack 外部となるアイテム。
     * @return 内部アイテム。
     */
    public static Optional<ItemStack> getInnerItem(String key, ItemStack stack) {
        NBTTagCompound compound = getCompound(key, stack);
        ItemStack inner = ItemStack.loadItemStackFromNBT(compound);
        return Optional.ofNullable(inner);
    }

    /**
     * 内部アイテムのセット。
     * @param key キー。
     * @param outer 外部となるアイテム。
     * @param inner 内部アイテム。
     */
    public static void setInnerItem(String key, ItemStack outer, ItemStack inner) {
        NBTTagCompound child = new NBTTagCompound();
        inner.writeToNBT(child);
        setCompound(key, outer, child);
    }
}
