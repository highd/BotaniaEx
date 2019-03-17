package com.github.highd120;

import com.github.highd120.achievement.AchievementTriggerer;
import com.github.highd120.achievement.AchievementsList;
import com.github.highd120.block.SubTileBindSword;
import com.github.highd120.block.SubTileFallingBlock;
import com.github.highd120.entity.EntitySword;
import com.github.highd120.item.ItemList;
import com.github.highd120.item.ShotSwordItem;
import com.github.highd120.proxy.CommonProxy;
import com.github.highd120.util.EnergyUtil;
import com.github.highd120.util.NBTTagUtil;
import com.github.highd120.util.gui.GuiManager;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileEntity;

@Mod(modid = BotaniaExMain.MOD_ID, version = BotaniaExMain.VERSION, dependencies = "required-after:Botania")
public class BotaniaExMain {
	public static final String MOD_ID = "botaniaex";
    public static final String MOD_NAME = "BotaniaEx";
	public static final String VERSION = "1.10.2";

	@Instance
	public static BotaniaExMain instance = new BotaniaExMain();

	@SidedProxy(clientSide = "com.github.highd120.proxy.ClientProxy",
			serverSide = "com.github.highd120.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
    public void preInit(FMLPreInitializationEvent event){
		ItemList.init(event);
		EntityRegistry.registerModEntity(EntitySword.class, MOD_ID + "sword", 1, this, 128, 5, true);
		BotaniaAPI.registerSubTile(SubTileBindSword.NAME, SubTileBindSword.class);
		BotaniaAPI.addSubTileToCreativeMenu(SubTileBindSword.NAME);
		BotaniaAPI.registerSubTile(SubTileFallingBlock.NAME, SubTileFallingBlock.class);
		BotaniaAPI.addSubTileToCreativeMenu(SubTileFallingBlock.NAME);
		RecipeList.init();
        proxy.registerRenderers();
		AchievementsList.init();
    }

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiManager());
		Lexicon.init();
	}

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    }
}
