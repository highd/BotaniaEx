package com.github.highd120.block;

import com.github.highd120.block.base.TileHasInventory;
import com.github.highd120.util.ItemUtil;

import net.minecraft.item.ItemStack;

public class TileStand extends TileHasInventory {
    /**
     * アイテムの取得。
     * @return アイテム。
     */
    public ItemStack getItem() {
        return itemHandler.getStackInSlot(0);
    }

    @Override
    protected SimpleItemStackHandler createItemStackHandler() {
        return new StandItemHandler(this, getInventorySize());
    }

    @Override
    public void update() {
    }

    /**
     * アイテムの設置のイベント。
     * @param stack プレイヤー。
     */
    public void action(ItemStack stack, boolean isCreative) {
        if (stack != null && getItem() == null) {
            ItemStack insertItem = stack.copy();
            insertItem.stackSize = 1;
            itemHandler.setStackInSlot(0, insertItem);
            if (!isCreative) {
                stack.stackSize--;
            }
        } else if (getItem() != null) {
            ItemUtil.dropItem(worldObj, pos.add(0, 1, 0), getItem());
            itemHandler.setStackInSlot(0, null);
        }
    }

    @Override
    public int getInventorySize() {
        return 1;
    }

    /**
     * スタンドのアイテムハンドラー。
     * @author hdgam
     *
     */
    private static class StandItemHandler extends SimpleItemStackHandler {
        public StandItemHandler(TileHasInventory inv, int limit) {
            super(inv, limit);
        }

        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

    }

    public void removeItem() {
        itemHandler.setStackInSlot(0, null);
    }
}
