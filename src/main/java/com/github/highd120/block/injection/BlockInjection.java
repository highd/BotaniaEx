package com.github.highd120.block.injection;

import com.github.highd120.block.BlockStand;
import com.github.highd120.util.block.BlockRegister;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import vazkii.botania.api.wand.IWandable;

@BlockRegister(name = "injection")
public class BlockInjection extends BlockStand implements IWandable {

    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
            EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX,
            float hitY, float hitZ) {
        TileInjection tile = (TileInjection) worldIn.getTileEntity(pos);
        playerIn.addChatComponentMessage(new TextComponentString("mana" + tile.getCurrentMana()));
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX,
                hitY,
                hitZ);
    }
}
