package com.github.highd120.proxy;

import com.github.highd120.BotaniaExMain;
import com.github.highd120.block.TileStand;
import com.github.highd120.block.TileStandRenderer;
import com.github.highd120.entity.EntitySword;
import com.github.highd120.entity.RenderEntitySwordFactory;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntitySword.class,
                new RenderEntitySwordFactory());
        ClientRegistry.bindTileEntitySpecialRenderer(TileStand.class, new TileStandRenderer());
    }

    @Override
    public void registerFluid() {
        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(BotaniaExMain.manaFluidBlock),
                new ItemMeshDefinition() {
                    @Override
                    public ModelResourceLocation getModelLocation(ItemStack stack) {
                        return BotaniaExMain.manaFluidModelLocation;
                    }
                });
        ModelLoader.setCustomStateMapper(BotaniaExMain.manaFluidBlock, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState blockState) {
                return BotaniaExMain.manaFluidModelLocation;
            }
        });
    }
}
