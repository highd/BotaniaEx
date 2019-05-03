package com.github.highd120.gui;

import javax.annotation.Nonnull;

import com.github.highd120.util.gui.GuiField;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.SlotItemHandler;
import vazkii.botania.client.gui.SlotLocked;

public class ContainerEditer extends Container {

    private InventoryEditer editerInventory;

    private int slotCount = 16;

    @GuiField
    private EntityPlayer player;

    public ContainerEditer(InventoryPlayer playerInv, InventoryEditer editerInventory) {
        this.editerInventory = editerInventory;
        initCommon(playerInv, editerInventory);
    }

    public ContainerEditer() {
    }

    /**
     * 初期化。
     */
    public void init() {
        InventoryPlayer playerInv = player.inventory;
        editerInventory = new InventoryEditer(player.getHeldItem(EnumHand.MAIN_HAND));
        initCommon(playerInv, editerInventory);
    }

    private void initCommon(InventoryPlayer playerInv, InventoryEditer editerInventory) {
        slotCount = editerInventory.getSlots();
        for (int i = 0; i < slotCount; ++i) {
            addSlotToContainer(new SlotItemHandler(editerInventory, i, 9, 26 + i * 18));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 154 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            if (playerInv.getStackInSlot(i) == editerInventory.getCalledItem()) {
                addSlotToContainer(new SlotLocked(playerInv, i, 8 + i * 18, 212));
            } else {
                addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 212));
            }
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return player.getHeldItemMainhand() == editerInventory.getCalledItem()
                || player.getHeldItemOffhand() == editerInventory.getCalledItem();
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemstack = null;
        Slot slot = inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex < 16) {
                if (!mergeItemStack(itemstack1, slotCount, slotCount + 36, true)) {
                    return null;
                }
            } else {
                if (!mergeItemStack(itemstack1, 0, slotCount, true)) {
                    return null;
                }
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

}