package com.github.highd120.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketBotaniaEffect.EffectType;
import vazkii.botania.common.network.PacketHandler;

public class WorldUtil {
    /**
     * ワールドからタイルエンティティを検索してリストにする。
     * @param world ワールド。
     * @param box 検索範囲。
     * @param isCast キャストできるか。
     * @return リスト。
     */
    public static <T extends TileEntity> List<T> scanTileEnity(World world, AxisAlignedBB box,
            Predicate<TileEntity> isCast) {
        List<T> list = new ArrayList<>();
        for (int x = (int) box.minX; x <= box.maxX; x++) {
            for (int y = (int) box.minY; y <= box.maxY; y++) {
                for (int z = (int) box.minZ; z <= box.maxZ; z++) {
                    TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                    if (tile == null || !isCast.test(tile)) {
                        continue;
                    }
                    @SuppressWarnings("unchecked")
                    T casted = (T) tile;
                    list.add(casted);
                }
            }
        }
        return list;
    }

    /**
     * Botaniaのエフェクトの再生。
     * @param type 種類。
     * @param postion 座標。
     * @param diff 座標との差分。
     * @param args 追加引数。
     */
    public static void playBotaniaEffect(World world, EffectType type, BlockPos postion, Vec3d diff,
            int... args) {
        double x = postion.getX() + diff.xCoord;
        double y = postion.getY() + diff.yCoord;
        double z = postion.getZ() + diff.zCoord;
        PacketBotaniaEffect effect = new PacketBotaniaEffect(type, x, y, z, args);
        PacketHandler.sendToNearby(world, postion, effect);
    }
}