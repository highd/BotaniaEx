package com.github.highd120.item;

import com.github.highd120.util.item.ItemRegister;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.rod.ItemCobbleRod;

@ItemRegister(name = "extraCobbleRod")
public class ItemExtraCobbleRod extends ItemCobbleRod implements HasInit {

    @Override
    protected boolean shouldRegister() {
        return false;
    }

    @Override
    public void init() {
        setUnlocalizedName("cobbleRod");
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if (state.getBlock() == Blocks.COBBLESTONE) {
            return 100;
        }
        return super.getStrVsBlock(stack, state);
    }

}
