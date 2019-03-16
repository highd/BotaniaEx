package com.github.highd120.proxy;

import com.github.highd120.BotaniaExMain;
import com.github.highd120.block.SubTileBindSword;
import com.github.highd120.block.SubTileFallingBlock;
import com.github.highd120.entity.EntitySword;
import com.github.highd120.entity.RenderEntitySword;
import com.github.highd120.entity.RenderEntitySwordFactory;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
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
}
