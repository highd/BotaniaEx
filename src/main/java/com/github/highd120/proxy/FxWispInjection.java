package com.github.highd120.proxy;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.botania.client.fx.FXWisp;

public class FxWispInjection extends FXWisp {
    /**
     * コンストラクター。
     * @param world ワールド。
     * @param initPoint 初期地点。
     */
    public FxWispInjection(World world, Vec3d initPoint) {
        super(world, initPoint.xCoord, initPoint.yCoord, initPoint.zCoord, 1, 0, 1, 0, true, true,
                1);
        particleAge = particleMaxAge / 2;
    }

    /**
     * コンストラクター。
     * @param world ワールド。
     * @param initPoint 初期地点。
     */
    public FxWispInjection(World world, Vec3d initPoint, int size, int lifeTime) {
        super(world, initPoint.xCoord, initPoint.yCoord, initPoint.zCoord, size, 0, 1, 0, true,
                true, lifeTime);
        particleAge = particleMaxAge / 2;
    }
}
