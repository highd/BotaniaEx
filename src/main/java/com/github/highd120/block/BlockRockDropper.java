package com.github.highd120.block;

import javax.annotation.Nonnull;

import com.github.highd120.util.block.BlockRegister;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;

@BlockRegister(name = "rockDropper")
public class BlockRockDropper extends BlockStand {
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
    public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");

    /**
     * コンストラクター。
     */
    public BlockRockDropper() {
        super();
        IBlockState state = blockState.getBaseState()
                .withProperty(BotaniaStateProps.FACING, EnumFacing.DOWN)
                .withProperty(TRIGGERED, false);

        setDefaultState(state);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return AABB;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
        boolean power = world.isBlockPowered(pos) || world.isBlockPowered(pos.up());
        boolean triggered = state.getValue(TRIGGERED).booleanValue();
        if (power && !triggered) {
            TileRockDropper tile = (TileRockDropper) world.getTileEntity(pos);
            tile.itemDropSingle();
            world.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)), 4);
        } else if (!power && triggered) {
            world.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)), 4);

        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state,
            EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        EnumFacing orientation = BlockPistonBase.getFacingFromEntity(pos, par5EntityLivingBase);
        world.setBlockState(pos, state.withProperty(BotaniaStateProps.FACING, orientation), 1 | 2);
    }

    @Nonnull
    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BotaniaStateProps.FACING, TRIGGERED);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(BotaniaStateProps.FACING).getIndex();
        if (state.getValue(TRIGGERED)) {
            meta |= 0b1000;
        }
        return meta;
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing face = EnumFacing.getFront(meta & 0b111);
        boolean triggered = (meta & 0b1000) == 1;
        return getDefaultState()
                .withProperty(BotaniaStateProps.FACING, face)
                .withProperty(TRIGGERED, triggered);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileRockDropper();
    }
}
