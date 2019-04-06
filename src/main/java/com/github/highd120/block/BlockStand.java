package com.github.highd120.block;

import javax.annotation.Nonnull;

import com.github.highd120.util.block.BlockRegister;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@BlockRegister(name = "stand")
public class BlockStand extends Block {

    private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 11.0 / 16.0, 1);

    /**
            * コンストラクター。
     */
    public BlockStand() {
        super(Material.ROCK);
        setHardness(3.5F);
        setSoundType(SoundType.STONE);
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileStand();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
            EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX,
            float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileStand tile = (TileStand) worldIn.getTileEntity(pos);
            tile.action(heldItem, playerIn.isCreative());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileStand tile = (TileStand) world.getTileEntity(pos);

        if (tile != null) {
            tile.breakEvent();
        }

        super.breakBlock(world, pos, state);
    }

    public void removeItem() {

    }
}
