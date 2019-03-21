package com.github.highd120.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * エンティティのユーティリティ。
 * @author hdgam
 */
public class EntityUtil {

    /**
     * エンティティの座標の取得。
     * @param entity エンティティ。
     * @return 座標。
     */
    public static Vec3d getPositon(Entity entity) {
        return new Vec3d(entity.posX, entity.posY, entity.posZ);
    }

}
