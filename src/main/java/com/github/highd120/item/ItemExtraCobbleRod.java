package com.github.highd120.item;

import java.util.List;

import com.github.highd120.util.InjectionUtil;
import com.github.highd120.util.NbtTagUtil;
import com.github.highd120.util.item.ItemRegister;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.rod.ItemCobbleRod;

@ItemRegister(name = "extraCobbleRod")
public class ItemExtraCobbleRod extends ItemCobbleRod implements HasInit {

    public static final String REMOVE_TAG = "remove";

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
        if (state.getBlock() == Blocks.COBBLESTONE
                && NbtTagUtil.getCompound(stack).hasKey("remove")) {
            return 100;
        }
        return super.getStrVsBlock(stack, state);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip,
            boolean advanced) {
        InjectionUtil.addInformation(stack, tooltip, new String[] { REMOVE_TAG });
    }

}
