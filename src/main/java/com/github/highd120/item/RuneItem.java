package com.github.highd120.item;

import com.github.highd120.util.item.ItemRegister;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.IFlowerComponent;

@ItemRegister(name = "rune")
public class RuneItem extends Item implements IFlowerComponent {

    @Override
    public boolean canFit(ItemStack stack, IPetalApothecary apothecary) {
        return true;
    }

    @Override
    public int getParticleColor(ItemStack stack) {
        return 0xA8A8A8;
    }
}
