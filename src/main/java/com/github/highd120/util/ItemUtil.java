package com.github.highd120.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * アイテムのユーティリティ。
 * @author hdgam
 */
public class ItemUtil {
    /**
     * アイテムをドロップさせる。
     * @param world ワールド。
     * @param postion ドロップさせる座標。
     * @param stack ドロップさせるアイテム。
     */
    public static void dropItem(World world, BlockPos postion, ItemStack stack) {
        if (world.isRemote) {
            return;
        }
        EntityItem result = new EntityItem(world, postion.getX(), postion.getY(), postion.getZ(),
                stack);
        world.spawnEntityInWorld(result);
    }
}
