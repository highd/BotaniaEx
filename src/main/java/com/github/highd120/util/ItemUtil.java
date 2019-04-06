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
     * @return アイテムのエンティティ。
     */
    public static EntityItem dropItem(World world, BlockPos postion, ItemStack stack) {
        if (world.isRemote || stack == null) {
            return null;
        }
        EntityItem result = new EntityItem(world, postion.getX(), postion.getY(), postion.getZ(),
                stack);
        world.spawnEntityInWorld(result);
        return result;
    }
}
