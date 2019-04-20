package com.github.highd120;

import com.github.highd120.achievement.AchievementsList;
import com.github.highd120.block.TileRockDropper;
import com.github.highd120.block.TileStand;
import com.github.highd120.block.injection.InjectionRecipe;
import com.github.highd120.block.injection.TileInjection;
import com.github.highd120.entity.EntitySword;
import com.github.highd120.list.FluidList;
import com.github.highd120.list.RecipeList;
import com.github.highd120.list.SoundList;
import com.github.highd120.network.NetworkHandler;
import com.github.highd120.proxy.CommonProxy;
import com.github.highd120.util.block.BlockManager;
import com.github.highd120.util.gui.GuiManager;
import com.github.highd120.util.item.ItemManager;
import com.github.highd120.util.subtile.SubTileManager;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * メインとなるクラス。
 * @author hdgam
 */
@Mod(modid = BotaniaExMain.MOD_ID, version = BotaniaExMain.VERSION,
        dependencies = "required-after:Botania")
public class BotaniaExMain {
    public static final String MOD_ID = "botaniaex";
    public static final String MOD_NAME = "BotaniaEx";
    public static final String VERSION = "1.10.2";

    @Instance
    public static BotaniaExMain instance = new BotaniaExMain();

    @SidedProxy(clientSide = "com.github.highd120.proxy.ClientProxy",
            serverSide = "com.github.highd120.proxy.CommonProxy")
    public static CommonProxy proxy;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    /**
     * 初期化。
     * @param event イベント。
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        BlockManager.init();
        ItemManager.init(event.getSide().isClient());
        SubTileManager.init(event.getSide().isClient());

        EntityRegistry.registerModEntity(EntitySword.class, MOD_ID + "sword", 1, this, 128, 5,
                true);
        RecipeList.init();
        InjectionRecipe.init();
        AchievementsList.init();
        NetworkHandler.init();
        FluidList.init();

        GameRegistry.registerTileEntity(TileStand.class, MOD_ID + ".stand");
        GameRegistry.registerTileEntity(TileInjection.class, MOD_ID + ".injection");
        GameRegistry.registerTileEntity(TileRockDropper.class, MOD_ID + ".rockDropper");

        SoundList.init();
        proxy.registerRenderers();
        proxy.registerFluid();
    }

    /**
     * 初期化。
     * @param event イベントデータ。
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiManager());
        Lexicon.init();
        GameRegistry.registerWorldGenerator(new WorldGenerator(), 0);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new DebugCommand());
    }
}
