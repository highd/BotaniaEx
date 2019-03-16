package com.github.highd120.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.text.NumberFormat;
import java.util.List;

import com.github.highd120.BotaniaExConfig;
import com.github.highd120.BotaniaExMain;
import com.github.highd120.gui.GuiWarp;
import com.github.highd120.util.EnergyUtil;
import com.github.highd120.util.gui.GuiManager;

import cofh.api.energy.IEnergyContainerItem;

public class WarpItem extends EnergyItem implements IRegiser{
	private int capacity = BotaniaExConfig.maxCapacity;

	public WarpItem() {
		super();
		setUnlocalizedName(BotaniaExMain.MOD_ID + "warp");
        setCreativeTab(CreativeTabs.TOOLS);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
		GuiManager.playerOpenGui(player, world, GuiWarp.class);
        return new ActionResult(EnumActionResult.PASS, itemStack);
	}

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return capacity;
    }

	@Override
	public String getName() {
		return "warp";
	}
}
