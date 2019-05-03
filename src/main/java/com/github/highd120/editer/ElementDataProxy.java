package com.github.highd120.editer;

import net.minecraft.nbt.NBTTagCompound;

public abstract class ElementDataProxy {
    protected String name;

    public ElementDataProxy(String name) {
        this.name = name;
    }

    public abstract void readNbt(NBTTagCompound compound);

    public abstract void writeNbt(NBTTagCompound compound);
}
