package com.github.highd120.editer;

import net.minecraft.nbt.NBTTagCompound;

public class NumberElementDataProxy extends ElementDataProxy {
    private int number = 0;

    public NumberElementDataProxy(String name) {
        super(name);
    }

    @Override
    public void readNbt(NBTTagCompound compound) {
        number = compound.getInteger(name);
    }

    @Override
    public void writeNbt(NBTTagCompound compound) {
        compound.setInteger(name, number);
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
