package com.github.highd120.gui;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class InventoryEditer implements IItemHandlerModifiable {

    private final IItemHandlerModifiable inventory;
    private final ItemStack calledItem;

    /**
     * コンストラクター。
     * @param calledItem 呼び出したアイテム。
     */
    public InventoryEditer(ItemStack calledItem) {
        this.calledItem = calledItem;
        inventory = (IItemHandlerModifiable) calledItem
                .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    public void setSize(int size) {

    }

    public ItemStack getCalledItem() {
        return calledItem;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        inventory.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return inventory.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return inventory.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return inventory.extractItem(slot, amount, simulate);
    }
}