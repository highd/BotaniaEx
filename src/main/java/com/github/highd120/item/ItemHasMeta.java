package com.github.highd120.item;

import java.util.List;

import javax.annotation.Nonnull;

import com.github.highd120.BotaniaExMain;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHasMeta extends ItemBase {
    private String[] meteNameList;

    public ItemHasMeta(String[] meteNameList) {
        this.meteNameList = meteNameList;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull Item item, CreativeTabs tab, List<ItemStack> stacks) {
        for (int i = 0; i < meteNameList.length; i++) {
            stacks.add(new ItemStack(item, 1, i));
        }
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return "item." + BotaniaExMain.MOD_ID + "." + meteNameList[Math.min(meteNameList.length - 1,
                par1ItemStack.getItemDamage())];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel() {
        for (int i = 0; i < meteNameList.length; i++) {
            String location = BotaniaExMain.MOD_ID + ":" + meteNameList[i];
            ModelLoader.setCustomModelResourceLocation(this, i,
                    new ModelResourceLocation(location, "inventory"));
        }
    }
}
