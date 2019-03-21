package com.github.highd120.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MathUtil {
    /**
     * AxisAlignedBBDCubeの作成。
     * @param postion 中心座標。
     * @param range 半径。
     * @return
     */
    public static AxisAlignedBB getAxisAlignedCube(BlockPos postion, int range) {
        return new AxisAlignedBB(postion.add(-range, -range, -range),
                postion.add(range + 1, range + 1, range + 1));
    }

    /**
     * 二つのベクトルの角度の計算。
     * @param vec1 ベクトル1
     * @param vec2 ベクトル2
     * @return
     */
    public static double angleCalculation(Vec3d vec1, Vec3d vec2) {
        double a = vec1.xCoord * vec2.xCoord + vec1.yCoord * vec2.yCoord + vec2.zCoord;
        double b = vec1.xCoord * vec1.xCoord + vec1.yCoord * vec1.yCoord
                + vec1.zCoord * vec1.zCoord;
        double c = vec2.xCoord * vec2.xCoord + vec2.yCoord * vec2.yCoord
                + vec2.zCoord * vec2.zCoord;
        return Math.toDegrees(Math.acos(a / (Math.sqrt(b) * Math.sqrt(c))));
    }

}
