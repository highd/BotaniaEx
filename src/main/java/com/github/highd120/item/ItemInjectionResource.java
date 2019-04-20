package com.github.highd120.item;

import com.github.highd120.util.item.ItemRegister;

@ItemRegister(name = "injectionResource")
public class ItemInjectionResource extends ItemHasMeta {
    private static String[] meteNameList = {
            "homing",
            "powerUp"
    };

    public ItemInjectionResource() {
        super(meteNameList);
    }

}
