package com.github.highd120.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MathUtil {
	public static AxisAlignedBB getAxisAlignedBBDCube(BlockPos postion, int range) {
		return new AxisAlignedBB(postion.add(-range, -range, -range), postion.add(range + 1, range + 1, range + 1));
	}
	public static double angleCalculation(Vec3d vec1, Vec3d vec2) {
		return Math.toDegrees( Math.acos((vec1.xCoord * vec2.xCoord + vec1.yCoord * vec2.yCoord + vec2.zCoord) /
				(Math.sqrt(vec1.xCoord * vec1.xCoord + vec1.yCoord * vec1.yCoord + vec1.zCoord * vec1.zCoord) *
				Math.sqrt(vec2.xCoord * vec2.xCoord + vec2.yCoord * vec2.yCoord + vec2.zCoord * vec2.zCoord))));
	}
}
