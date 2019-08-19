package com.github.highd120.block.injection;

import net.minecraft.item.ItemStack;

public class InjectionExtraAddTag extends InjectionAddTag {

    public InjectionExtraAddTag(Input input, ItemStack output, int useMana, String tag, Type type,
            int minLv) {
        super(input, output, useMana, tag, type, minLv);
    }
}
