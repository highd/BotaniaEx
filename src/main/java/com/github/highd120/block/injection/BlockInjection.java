package com.github.highd120.block.injection;

import javax.annotation.Nonnull;

import com.github.highd120.Lexicon;
import com.github.highd120.block.BlockStand;
import com.github.highd120.util.block.BlockRegister;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandable;

@BlockRegister(name = "injection")
public class BlockInjection extends BlockStand implements IWandable, ILexiconable {

    private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);

    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return AABB;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileInjection();
    }

    @Override
    public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos,
            EnumFacing side) {
        TileInjection tile = (TileInjection) world.getTileEntity(pos);
        if (tile != null) {
            tile.active();
        }
        return false;
    }

    @Override
    public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player,
            ItemStack lexicon) {
        return Lexicon.injectionEntry;
    }
}
