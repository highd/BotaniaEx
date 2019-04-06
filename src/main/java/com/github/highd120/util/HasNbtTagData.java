package com.github.highd120.util;

import net.minecraft.nbt.NBTTagCompound;

public interface HasNbtTagData {
    public default void readNbt(NBTTagCompound tag) {
        NBTTagCompound child = tag.getCompoundTag(getName());
        subReadNbt(child);
    }

    /**
     * NBTタグへの書き込み。
     * @param tag タグ。
     */
    public default void writeNbt(NBTTagCompound tag) {
        NBTTagCompound child = new NBTTagCompound();
        subWriteNbt(child);
        tag.setTag(getName(), child);
    }

    void subReadNbt(NBTTagCompound tag);

    void subWriteNbt(NBTTagCompound tag);

    String getName();
}
