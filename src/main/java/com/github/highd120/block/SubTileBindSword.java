package com.github.highd120.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.lwjgl.opengl.GL11;

import com.github.highd120.BotaniaExMain;
import com.github.highd120.item.ItemList;
import com.github.highd120.util.CollectionUtil;
import com.github.highd120.util.Constant;
import com.github.highd120.util.MathUtil;
import com.github.highd120.util.NBTTagUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.sound.BotaniaSoundEvents;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;

public class SubTileBindSword extends SubTileFunctional {
	public static class LockEntity{
		private Entity entity;
		private BlockPos postion;
		public LockEntity(Entity entity, BlockPos postion) {
			this.entity = entity;
			this.postion = postion;
		}
		void UpDate() {
			if (entity != null) {
				entity.setPosition(postion.getX()+0.5, postion.getY()+0.5, postion.getZ());
				if (entity.isDead) {
					entity = null;
				}
			}
		}
	}
	public static final String NAME = "bindsword";
	private static final int RANGE = 1;
	private boolean isDisassemble = false;
	private List<Runnable> runnableList = new ArrayList<>();
	private List<LockEntity> entityList = new ArrayList<>();
	@Override
	public void onUpdate() {
		super.onUpdate();
		runnableList.forEach(Runnable::run);
		runnableList.clear();
		entityList.forEach(LockEntity::UpDate);
		if (mana < getMaxMana()) return;
		List<EntityItem> items = getWorld().getEntitiesWithinAABB(EntityItem.class, MathUtil.getAxisAlignedBBDCube(getPos(), RANGE));
		if (isDisassemble) {
			Disassemble(items);
		} else {
			Assemble(items);
		}
	}

	private void addDisassembleItemRunnable(ItemStack itemStack) {
		runnableList.add(()->{
			ItemStack inner = NBTTagUtil.getInnerItem(Constant.SHOT_SWORD_TAG, itemStack).get();
			NBTTagUtil.removeTag(Constant.SHOT_SWORD_TAG, itemStack);
			EntityItem result = new EntityItem(getWorld(), getPos().getX(), getPos().getY(), getPos().getZ(), inner);
			getWorld().spawnEntityInWorld(result);
			mana = 0;
		});
	}

	private void Disassemble(List<EntityItem> items) {
		CollectionUtil.findIf(items, item->{
			ItemStack itemStack = item.getEntityItem();
			if (itemStack.getItem() != ItemList.shotSwordItem) return false;
			Optional<ItemStack> inner = NBTTagUtil.getInnerItem(Constant.SHOT_SWORD_TAG, itemStack);
			return inner.isPresent();
		}).ifPresent(item->{
			entityList.add(new LockEntity(item, getPos().add(0, 1, 0)));
			if (this.getWorld().isRemote) {
				getWorld().spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, item.posX, item.posY, item.posZ, 0.0D, 0.0D, 0.0D, 10);
			} else {
				getWorld().playSound(null, getPos(), BotaniaSoundEvents.terrasteelCraft, SoundCategory.BLOCKS, 0.2F, 1F);
				addDisassembleItemRunnable(item.getEntityItem());
			}
		});
	}
	private void Assemble(List<EntityItem> items) {
		EntityItem dropShotSword = CollectionUtil.findIf(items, item->{
			ItemStack itemStack = item.getEntityItem();
			if (itemStack.getItem() != ItemList.shotSwordItem) return false;
			Optional<ItemStack> inner = NBTTagUtil.getInnerItem(Constant.SHOT_SWORD_TAG, itemStack);
			return !inner.isPresent();
		}).orElse(null);
		EntityItem sword = CollectionUtil.findIf(items, item->{
			return item.getEntityItem().getItem() instanceof ItemSword;
		}).orElse(null);
		if (dropShotSword == null || sword == null) return;
		mana = 0;
		entityList.add(new LockEntity(dropShotSword, getPos().add(0, 1, 0)));
		if (this.getWorld().isRemote) {
			getWorld().spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, dropShotSword.posX, dropShotSword.posY, dropShotSword.posZ, 0.0D, 0.0D, 0.0D, 10);
		} else {
			runnableList.add(()->{
				getWorld().playSound(null, getPos(), BotaniaSoundEvents.terrasteelCraft, SoundCategory.BLOCKS, 0.2F, 1F);
				NBTTagUtil.setInnerItem(Constant.SHOT_SWORD_TAG, dropShotSword.getEntityItem(), sword.getEntityItem());
				sword.setDead();
			});
		}
	}
	@Override
	public int getMaxMana() {
		return 10000;
	}
	@Override
	public boolean onWanded(EntityPlayer player, ItemStack wand) {
		if(player == null)
			return false;

		if(player.isSneaking()) {
			isDisassemble = !isDisassemble;
			sync();
			return true;
		}
		return super.onWanded(player, wand);
	}
	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}
	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		super.renderHUD(mc, res);

		int color = getColor();
		String filter = isDisassemble ? "disassemble" : "assemble";
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int x = res.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(filter) / 2;
		int y = res.getScaledHeight() / 2 + 30;

		mc.fontRendererObj.drawStringWithShadow(filter, x, y, color);
		GlStateManager.disableBlend();
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);
		isDisassemble = cmp.getBoolean(NAME);
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);
		cmp.setBoolean(NAME, isDisassemble);
	}
}
