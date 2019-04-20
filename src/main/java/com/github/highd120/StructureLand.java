package com.github.highd120;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.github.highd120.list.FluidList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AltGrassVariant;
import vazkii.botania.common.block.ModBlocks;

public class StructureLand extends WorldGenerator {

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        DebugCommand.debug(position);
        if (worldIn.isRemote) {
            return false;
        }
        position = position.add(0, 2, 0);
        Random random = new Random();
        createCircle(position, worldIn, 2, random);
        createCircle(position.add(0, 1, 0), worldIn, 4, random);
        createCircle(position.add(0, 2, 0), worldIn, 6, random);
        List<BlockPos> list = createLast(position.add(0, 3, 0), worldIn, 7, random);
        createLake(position.add(0, 5, 0), worldIn, 3, random, list);
        Collections.shuffle(list, random);
        list = createWood(worldIn, random, list, new int[] { 0, 0, 0, 0 });
        list = createWood(worldIn, random, list, new int[] { 8, 12, 12, 14 });
        for (int i = 0; i < 16; i++) {
            BlockPos pos1 = list.get(0);
            list.remove(0);
            EnumDyeColor color = EnumDyeColor.byMetadata(i);
            IBlockState flower = ModBlocks.flower.getDefaultState()
                    .withProperty(BotaniaStateProps.COLOR, color);
            worldIn.setBlockState(pos1, flower, 2);

        }
        return true;
    }

    private void createCircle(BlockPos pos, World world, int radius, Random random) {
        IBlockState livingrock = ModBlocks.livingrock.getDefaultState();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z < radius * radius + random.nextDouble() * 4 - 2) {
                    world.setBlockState(pos.add(x, 0, z), livingrock, 2);
                }
            }

        }
    }

    private void createLake(BlockPos pos, World world, int radius, Random random,
            List<BlockPos> list) {
        IBlockState manaWater = FluidList.manaFluid.getBlock().getDefaultState();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z < radius * radius + random.nextDouble() * 4 - 2) {
                    world.setBlockState(pos.add(x, 0, z), manaWater, 2);
                    list.remove(pos.add(x, 1, z));
                }
            }

        }
    }

    private List<BlockPos> createLast(BlockPos pos, World world, int radius, Random random) {
        IBlockState dirt = ModBlocks.altGrass.getDefaultState()
                .withProperty(BotaniaStateProps.ALTGRASS_VARIANT, AltGrassVariant.INFUSED);
        IBlockState livingwood = ModBlocks.livingwood.getDefaultState();
        IBlockState livingrock = ModBlocks.livingrock.getDefaultState();
        List<BlockPos> list = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z < radius * radius + random.nextDouble() * 4 - 2) {
                    world.setBlockState(pos.add(x, 0, z), livingrock, 2);
                    world.setBlockState(pos.add(x, 1, z), livingwood, 2);
                    world.setBlockState(pos.add(x, 2, z), dirt, 2);
                    if (-6 <= x && x <= 6 && -6 <= z && z <= 6) {
                        list.add(pos.add(x, 3, z));
                    }
                }
            }

        }
        return list;
    }

    private IBlockState getPetal(int n) {
        EnumDyeColor color = EnumDyeColor.byMetadata(n);
        return ModBlocks.petalBlock.getDefaultState().withProperty(BotaniaStateProps.COLOR, color);
    }

    private List<BlockPos> createWood(World world, Random random, List<BlockPos> list,
            int[] color) {
        IBlockState livingwood = ModBlocks.livingwood.getDefaultState();
        if (list.isEmpty()) {
            return list;
        }
        BlockPos pos = list.get(0);
        for (int i = 0; i < 6; i++) {
            world.setBlockState(pos.add(0, i, 0), livingwood, 2);
        }
        for (int i = 2; i < 6; i++) {
            world.setBlockState(pos.add(-1, i, 0), getPetal(color[0]), 2);
        }
        for (int i = 2; i < 6; i++) {
            world.setBlockState(pos.add(1, i, 0), getPetal(color[1]), 2);
        }
        for (int i = 2; i < 6; i++) {
            world.setBlockState(pos.add(0, i, -1), getPetal(color[2]), 2);
        }
        for (int i = 2; i < 6; i++) {
            world.setBlockState(pos.add(0, i, 1), getPetal(color[3]), 2);
        }
        return list.stream()
                .filter(pos2 -> pos.distanceSq(pos2) > 4)
                .collect(Collectors.toList());
    }

}
