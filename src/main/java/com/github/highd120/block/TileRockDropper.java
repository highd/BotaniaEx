package com.github.highd120.block;

import com.github.highd120.util.ItemUtil;
import com.github.highd120.util.MathUtil;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import vazkii.botania.api.state.BotaniaStateProps;

public class TileRockDropper extends TileStand {

    /**
     * アイテムをドロップさせる。
     */
    public void itemDropSingle() {
        ItemStack item = itemHandler.getStackInSlot(0);
        EnumFacing face = getWorld().getBlockState(getPos()).getValue(BotaniaStateProps.FACING);
        if (item != null && !getWorld().isRemote) {
            itemHandler.addItemStock(0, -1);
            ItemStack dropItem = item.copy();
            dropItem.stackSize = 1;
            Vec3d offset = MathUtil.vec3iToVec3d(face.getDirectionVec());
            Vec3d postion = MathUtil.blockPosToVec3dCenter(getPos()).add(offset);
            EntityItem result = ItemUtil.dropItem(getWorld(), postion, dropItem);
            result.setVelocity(0, 0, 0);
            result.setPosition(postion.xCoord, postion.yCoord, postion.zCoord);
            result.setPickupDelay(100);
        }
    }

}
