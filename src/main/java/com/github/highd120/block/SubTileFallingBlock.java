package com.github.highd120.block;

import java.util.List;

import com.github.highd120.util.MathUtil;
import com.github.highd120.util.subtile.SubTileRegister;

import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.math.Vec3d;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;

/**
 * 落下エンティティでマナを起こす花。
 * @author hdgam
 */
@SubTileRegister(name = SubTileFallingBlock.NAME)
public class SubTileFallingBlock extends SubTileGenerating {
    public static final String NAME = "fallingblock";
    private static final int RANGE = 1;

    @Override
    public void onUpdate() {
        super.onUpdate();
        List<EntityFallingBlock> blocks = getWorld().getEntitiesWithinAABB(EntityFallingBlock.class,
                MathUtil.getAxisAlignedCube(getPos(), RANGE));
        blocks.forEach(block -> {
            if (!getWorld().isRemote) {
                Vec3d speed = new Vec3d(block.motionX, block.motionY, block.motionZ);
                double angle = MathUtil.angleCalculation(speed, new Vec3d(0, -1, 0));
                int generateMana = (int) Math.floor(angle * angle / 4 + 30);
                mana += generateMana;
                block.setDead();
            }
        });
    }

    @Override
    public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(toBlockPos(), RANGE);
    }

    @Override
    public int getMaxMana() {
        return 20000;
    }
}
