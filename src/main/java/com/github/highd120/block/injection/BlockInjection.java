package com.github.highd120.block.injection;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockInjection extends Block {
    public BlockInjection() {
        super(Material.ROCK);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

}
