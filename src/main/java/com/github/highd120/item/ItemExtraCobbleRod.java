package com.github.highd120.item;

import java.util.List;

import com.github.highd120.util.Constant;
import com.github.highd120.util.NbtTagUtil;
import com.github.highd120.util.item.ItemRegister;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
        if (state.getBlock() == Blocks.COBBLESTONE
                && NbtTagUtil.getCompound(stack).hasKey("remove")) {
            return 100;
        }
        return super.getStrVsBlock(stack, state);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip,
            boolean advanced) {
        NBTTagCompound child = NbtTagUtil.getCompound(Constant.SHOT_SWORD_TAG, stack);
        ItemStack inner = ItemStack.loadItemStackFromNBT(child);
        if (NbtTagUtil.getCompound(stack).hasKey("remove")) {
            tooltip.add(I18n.format("botaniaex.injection.remove"));
        }
    }

}
