package com.github.highd120.proxy;

import com.github.highd120.BotaniaExMain;

import com.github.highd120.block.SubTileBindSword;
import com.github.highd120.block.SubTileFallingBlock;
import com.github.highd120.entity.EntitySword;
import com.github.highd120.entity.RenderEntitySword;
import com.github.highd120.entity.RenderEntitySwordFactory;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntitySword.class, new RenderEntitySwordFactory());
		BotaniaAPIClient.registerSubtileModel(SubTileBindSword.class, new ModelResourceLocation(BotaniaExMain.MOD_ID + ":" + SubTileBindSword.NAME));
		BotaniaAPIClient.registerSubtileModel(SubTileFallingBlock.class, new ModelResourceLocation(BotaniaExMain.MOD_ID + ":" + SubTileFallingBlock.NAME));

	}

	@Override
	public void registerFluid() {
        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(BotaniaExMain.manaFluidBlock), new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return BotaniaExMain.manaFluidModelLocation;
            }
        });
        ModelLoader.setCustomStateMapper(BotaniaExMain.manaFluidBlock, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
                return BotaniaExMain.manaFluidModelLocation;
            }
        });
	}
}
