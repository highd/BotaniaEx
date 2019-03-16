package com.github.highd120.proxy;

import com.github.highd120.KeyConfigEditer;
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
        BotaniaAPIClient.registerSubtileModel(SubTileBindSword.NAME,
                new ModelResourceLocation(new ResourceLocation(KeyConfigEditer.MOD_ID, SubTileBindSword.NAME), "normal"),
                new ModelResourceLocation(new ResourceLocation(KeyConfigEditer.MOD_ID, SubTileBindSword.NAME), "inventory"));
		RenderingRegistry.registerEntityRenderingHandler(EntitySword.class, new RenderEntitySwordFactory());
		BotaniaAPIClient.registerSubtileModel(SubTileBindSword.class, new ModelResourceLocation("extrawarp:" + SubTileBindSword.NAME));
		BotaniaAPIClient.registerSubtileModel(SubTileFallingBlock.class, new ModelResourceLocation("extrawarp:" + SubTileFallingBlock.NAME));
	}
}
