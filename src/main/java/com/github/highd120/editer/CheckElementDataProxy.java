package com.github.highd120.editer;

import net.minecraft.nbt.NBTTagCompound;

public class CheckElementDataProxy extends ElementDataProxy {
    private boolean has = false;

    public CheckElementDataProxy(String name) {
        super(name);
    }

    @Override
    public void readNbt(NBTTagCompound compound) {
        has = compound.hasKey(name);
    }

    @Override
    public void writeNbt(NBTTagCompound compound) {
        if (has) {
            compound.setInteger(name, 0);
        } else {
            compound.removeTag(name);
        }
    }

    public void change() {
        has = !has;
    }

    public boolean isHas() {
        return has;
    }
}
