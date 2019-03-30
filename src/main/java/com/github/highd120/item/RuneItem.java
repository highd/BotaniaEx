package com.github.highd120.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.IFlowerComponent;

public class RuneItem extends Item implements IFlowerComponent, IRegiser {

    @Override
    public boolean canFit(ItemStack stack, IPetalApothecary apothecary) {
        return true;
    }

    @Override
    public int getParticleColor(ItemStack stack) {
        return 0xA8A8A8;
    }

    @Override
    public String getName() {
        return "rune";
    }

}
