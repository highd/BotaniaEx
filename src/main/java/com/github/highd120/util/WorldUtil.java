package com.github.highd120.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldUtil {
    /**
     * ワールドからタイルエンティティを検索してリストにする。
     * @param world ワールド。
     * @param box 検索範囲。
     * @param isCast キャストできるか。
     * @param run ランナー。
     * @return リスト。
     */
    public static <T extends TileEntity, U> List<U> scanTileEnity(World world, AxisAlignedBB box,
            Predicate<TileEntity> isCast, Function<T, Optional<U>> run) {
        List<U> list = new ArrayList<>();
        for (int x = (int) box.minX; x <= box.maxX; x++) {
            for (int y = (int) box.minY; y <= box.maxY; y++) {
                for (int z = (int) box.minZ; z <= box.maxZ; z++) {
                    TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                    if (tile == null || !isCast.test(tile)) {
                        continue;
                    }
                    @SuppressWarnings("unchecked")
                    T casted = (T) tile;
                    run.apply(casted).ifPresent(result -> {
                        list.add(result);
                    });
                }
            }
        }
        return list;
    }
}