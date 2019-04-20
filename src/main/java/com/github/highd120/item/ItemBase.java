package com.github.highd120.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.common.core.BotaniaCreativeTab;

public class ItemBase extends Item {
    public ItemBase() {
        setCreativeTab(BotaniaCreativeTab.INSTANCE);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
