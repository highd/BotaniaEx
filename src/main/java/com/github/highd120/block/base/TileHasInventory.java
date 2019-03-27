package com.github.highd120.block.base;

import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import com.github.highd120.util.ItemUtil;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.botania.api.internal.VanillaPacketDispatcher;

public abstract class TileHasInventory extends TileEntityBase {
    protected ItemStackHandler itemHandler = createItemStackHandler();

    public abstract int getInventorySize();

    protected ItemStackHandler createItemStackHandler() {
        return new SimpleItemStackHandler(this, getInventorySize());
    }

    @Override
    public void subReadNbt(NBTTagCompound compound) {
        super.subReadNbt(compound);
        itemHandler = createItemStackHandler();
        itemHandler.deserializeNBT(compound);
    }

    @Override
    public void subWriteNbt(NBTTagCompound compound) {
        super.subWriteNbt(compound);
        compound.merge(itemHandler.serializeNBT());
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> cap, @Nonnull EnumFacing side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || super.hasCapability(cap, side);
    }

    @Nonnull
    @Override
    public <T> T getCapability(@Nonnull Capability<T> cap, @Nonnull EnumFacing side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
        }
        return super.getCapability(cap, side);
    }

    /**
            * ブロックの破壊時のイベント。
     */
    public void breakEvent() {
        IntStream.range(0, itemHandler.getSlots()).forEach(i -> {
            ItemUtil.dropItem(worldObj, pos, itemHandler.getStackInSlot(i));
        });
    }

    protected static class SimpleItemStackHandler extends ItemStackHandler {
        private final TileHasInventory tile;

        public SimpleItemStackHandler(TileHasInventory inv, int limit) {
            super(limit);
            tile = inv;
        }

        @Override
        public void onContentsChanged(int slot) {
            if (!tile.worldObj.isRemote) {
                VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
                tile.worldObj.scheduleUpdate(tile.pos, tile.getBlockType(),
                        tile.getBlockType().tickRate(tile.worldObj));
            }
            tile.markDirty();
        }
    }
}
