package com.github.highd120;

import com.github.highd120.achievement.AchievementsList;
import com.github.highd120.block.SubTileBindSword;
import com.github.highd120.block.SubTileCreateManaFluid;
import com.github.highd120.block.SubTileFallingBlock;
import com.github.highd120.block.injection.InjectionRecipe;
import com.github.highd120.entity.EntitySword;
import com.github.highd120.item.ItemList;
import com.github.highd120.proxy.CommonProxy;
import com.github.highd120.util.gui.GuiManager;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
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
import vazkii.botania.api.BotaniaAPI;

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

    public static Block manaFluidBlock;

    public static Fluid manaFluid = new Fluid("mana_fluid2",
            new ResourceLocation(BotaniaExMain.MOD_ID + ":blocks/mana_fluid_still"),
            new ResourceLocation(BotaniaExMain.MOD_ID + ":blocks/mana_fluid_flow"));

    public static ModelResourceLocation manaFluidModelLocation = new ModelResourceLocation(
            BotaniaExMain.MOD_ID + ":mana_fluid_block", "fluid");

    static {
        FluidRegistry.enableUniversalBucket();
    }

    /**
     * 初期化。
     * @param event イベント。
     */
    @SuppressWarnings("deprecation")
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ItemList.init(event);
        EntityRegistry.registerModEntity(EntitySword.class, MOD_ID + "sword", 1, this, 128, 5,
                true);
        BotaniaAPI.registerSubTile(SubTileBindSword.NAME, SubTileBindSword.class);
        BotaniaAPI.addSubTileToCreativeMenu(SubTileBindSword.NAME);
        BotaniaAPI.registerSubTile(SubTileFallingBlock.NAME, SubTileFallingBlock.class);
        BotaniaAPI.addSubTileToCreativeMenu(SubTileFallingBlock.NAME);
        BotaniaAPI.registerSubTile(SubTileCreateManaFluid.NAME, SubTileCreateManaFluid.class);
        BotaniaAPI.addSubTileToCreativeMenu(SubTileCreateManaFluid.NAME);
        RecipeList.init();
        InjectionRecipe.init();
        proxy.registerRenderers();
        AchievementsList.init();
        FluidRegistry.registerFluid(manaFluid);
        manaFluidBlock = new BlockFluidClassic(manaFluid, Material.WATER);
        manaFluidBlock.setUnlocalizedName(MOD_ID + ":mana_fluid");
        manaFluid.setBlock(manaFluidBlock);
        GameRegistry.registerBlock(manaFluidBlock, MOD_ID + ":mana_fluid_block");
        proxy.registerFluid();
        FluidRegistry.addBucketForFluid(BotaniaExMain.manaFluid);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiManager());
        Lexicon.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new DebugCommand());
    }
}
