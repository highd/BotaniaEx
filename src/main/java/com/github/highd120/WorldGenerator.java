package com.github.highd120;

import java.util.Random;

import com.github.highd120.util.WorldUtil;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenerator implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world,
            IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (!(world.provider instanceof WorldProviderSurface)) {
            return;
        }
        generateLand(chunkX * 16, chunkZ * 16, random, world);
    }

    private void generateLand(int x, int z, Random random, World world) {
        if (!(BotaniaExConfig.createLand)) {
            return;
        }
        BlockPos pos1 = world.getHeight(new BlockPos(x + 8, 0, z + 8)).add(0, 30, 0);
        if (pos1.getY() > 244) {
            return;
        }
        boolean isValid = WorldUtil
                .getPostionList(new AxisAlignedBB(pos1.add(-7, 0, -7), pos1.add(7, 11, 7)))
                .stream()
                .allMatch(postion -> world.isAirBlock(postion));
        if (random.nextInt(BotaniaExConfig.landSpawnRate) == 0 && isValid) {
            new StructureLand().generate(world, random, pos1);
        }
    }

}
