package com.github.highd120.jei;

import com.github.highd120.item.ItemExtra;

import mezz.jei.api.ISubtypeRegistry;
import net.minecraft.item.ItemStack;

public class ItemExtraSubtypeInterpreter implements ISubtypeRegistry.ISubtypeInterpreter {
    @Override
    public String getSubtypeInfo(ItemStack itemStack) {
        System.out.println("useNbtForSubtypes start==============================");
        System.out.println(itemStack.getDisplayName());
        System.out.println("useNbtForSubtypes end==============================");
        if (itemStack.getItem() instanceof ItemExtra) {
            return Integer.toString(ItemExtra.getProperty(itemStack));
        }
        return null;
    }

}
