package com.github.highd120;

import java.util.Random;

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
        BlockPos pos1 = world.getHeight(new BlockPos(x, 0, z)).add(0, 30, 0);
        if (random.nextInt(40) == 0) {
            new StructureLand().generate(world, random, pos1);
        }
    }

}
