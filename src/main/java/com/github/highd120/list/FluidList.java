package com.github.highd120.list;

import java.util.ArrayList;
import java.util.List;

import com.github.highd120.BotaniaExMain;

import lombok.AllArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FluidList {
    @AllArgsConstructor
    private static class Data {
        private String name;
        private Block block;
    }

    private static List<Data> dataList = new ArrayList<>();
    public static Fluid manaFluid;

    public static void init() {
        manaFluid = registerFluid("ex_mana_fluid");
    }

    /**
     * クライアントの初期化。、
     */
    public static void initClient() {
        dataList.forEach(data -> {
            ModelResourceLocation modelLocation = new ModelResourceLocation(
                    BotaniaExMain.MOD_ID + ":" + data.name + "_block", "fluid");
            ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(data.block),
                    new ItemMeshDefinition() {
                        @Override
                        public ModelResourceLocation getModelLocation(ItemStack stack) {
                            return modelLocation;
                        }
                    });
            ModelLoader.setCustomStateMapper(data.block, new StateMapperBase() {
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState blockState) {
                    return modelLocation;
                }
            });
        });
        dataList = null;
    }

    private static Fluid registerFluid(String name) {
        ResourceLocation still = new ResourceLocation(
                BotaniaExMain.MOD_ID + ":blocks/" + name + "_still");
        ResourceLocation flow = new ResourceLocation(
                BotaniaExMain.MOD_ID + ":blocks/" + name + "_flow");
        Fluid fluid = new Fluid(name, still, flow);
        FluidRegistry.registerFluid(fluid);
        Block block = new BlockFluidClassic(fluid, Material.WATER);
        block.setRegistryName(new ResourceLocation(BotaniaExMain.MOD_ID, name + "_block"));
        block.setUnlocalizedName(BotaniaExMain.MOD_ID + ":" + name + "_block");
        fluid.setBlock(block);
        GameRegistry.register(block);
        ItemBlock item = new ItemBlock(block);
        item.setRegistryName(block.getRegistryName());
        item.setUnlocalizedName(block.getUnlocalizedName());
        GameRegistry.register(item);
        FluidRegistry.addBucketForFluid(fluid);
        dataList.add(new Data(name, block));
        return fluid;
    }
}
