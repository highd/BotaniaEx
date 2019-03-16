package com.github.highd120.item;

import java.util.List;

import com.github.highd120.entity.EntitySword;
import com.github.highd120.gui.GuiTest;
import com.github.highd120.util.Constant;
import com.github.highd120.util.NBTTagUtil;
import com.github.highd120.util.gui.GuiManager;
import com.github.highd120.achievement.AchievementsList;
import com.github.highd120.achievement.ICraftedAchievement;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;

public class ShotSwordItem extends Item implements IManaUsingItem, ICraftedAchievement, IRegiser{
	public ShotSwordItem() {
		setMaxStackSize(1);
        setNoRepair();
        setCreativeTab(CreativeTabs.COMBAT);
	}
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote && ManaItemHandler.requestManaExactForTool(itemStack, player, 1000, true)) {
			NBTTagCompound child = NBTTagUtil.getCompound(Constant.SHOT_SWORD_TAG, itemStack);
			NBTTagCompound entityTag = new NBTTagCompound();
			EntitySword sword = new EntitySword(world, player);
			world.spawnEntityInWorld(sword);
			sword.writeEntityToNBT(entityTag);
			entityTag.setTag(Constant.SHOT_SWORD_TAG, child);
			sword.readEntityFromNBT(entityTag);
		}
        return new ActionResult(EnumActionResult.PASS, itemStack);
	}

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    	NBTTagCompound child = NBTTagUtil.getCompound(Constant.SHOT_SWORD_TAG, stack);
    	ItemStack inner = ItemStack.loadItemStackFromNBT(child);
    	if (inner != null) {
            tooltip.add(">" + inner.getDisplayName());
    	}
    }

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
	@Override
	public Achievement getAchievement() {
		return AchievementsList.shotSword;
	}
	@Override
	public String getName() {
		return "shot_sword";
	}
}
