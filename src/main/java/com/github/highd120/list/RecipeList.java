package com.github.highd120.list;

import com.github.highd120.block.BlockRockDropper;
import com.github.highd120.block.BlockStand;
import com.github.highd120.block.SubTileBindSword;
import com.github.highd120.block.SubTileCreateManaFluid;
import com.github.highd120.block.SubTileFallingBlock;
import com.github.highd120.block.injection.BlockInjection;
import com.github.highd120.item.ItemExtra;
import com.github.highd120.item.ItemInjectionResource;
import com.github.highd120.item.RuneItem;
import com.github.highd120.item.ShotSwordItem;
import com.github.highd120.util.item.ItemManager;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

/**
 * レシピのリスト。
 * @author hdgam
 */
public class RecipeList {
    public static RecipeRuneAltar runeRecipe;
    public static RecipeRuneAltar injectionCoreRecipe;
    public static ShapedOreRecipe standCoreRecipe;
    public static ShapedOreRecipe homingRecipe;
    public static ShapedOreRecipe shotSwordRecipe;
    public static RecipePetals bindSwordFlowerRecipe;
    public static RecipePetals createManaFlowerRecipe;
    public static RecipePetals fallingBlockRecipe;
    public static ShapedOreRecipe rockDropperRecipe;
    public static ShapedOreRecipe extraRecipe;

    /**
     * レシピの登録。
     */
    public static void init() {
        runeRecipe = BotaniaAPI.registerRuneAltarRecipe(ItemManager.getItemStack(RuneItem.class),
                5000,
                "runeWaterB", "runeFireB", "runeEarthB", "runeAirB",
                "runeSpringB", "runeSummerB", "runeAutumnB", "runeWinterB",
                "runeManaB", "runeLustB", "runeGluttonyB", "runeGreedB",
                "runeSlothB", "runeWrathB", "runeEnvyB", "runePrideB");

        injectionCoreRecipe = BotaniaAPI.registerRuneAltarRecipe(
                ItemManager.getItemStack(BlockInjection.class),
                1000,
                new ItemStack(ModBlocks.shimmerrock), "livingwood",
                new ItemStack(ModBlocks.shimmerrock), "livingwood",
                new ItemStack(ModBlocks.shimmerrock), "livingwood",
                new ItemStack(ModBlocks.shimmerrock), "livingwood",
                ItemManager.getItemStack(RuneItem.class));

        homingRecipe = new ShapedOreRecipe(
                ItemManager.getItemStack(ItemInjectionResource.class, 0),
                new Object[] {
                        "XXX",
                        "XYX",
                        "XXX",
                        Character.valueOf('X'),
                        "stone",
                        Character.valueOf('Y'),
                        new ItemStack(ModItems.crystalBow) });

        standCoreRecipe = new ShapedOreRecipe(ItemManager.getItemStack(BlockStand.class),
                new Object[] {
                        "XXX",
                        " Y ",
                        "ZZZ",
                        Character.valueOf('X'),
                        "plankWood",
                        Character.valueOf('Y'),
                        "logWood",
                        Character.valueOf('Z'),
                        "cobblestone" });
        shotSwordRecipe = new ShapedOreRecipe(ItemManager.getItemStack(ShotSwordItem.class),
                new Object[] {
                        " X ",
                        "ZYZ",
                        "   ",
                        Character.valueOf('X'),
                        new ItemStack(Items.DIAMOND_SWORD, 1, 0),
                        Character.valueOf('Y'),
                        ItemManager.getItemStack(RuneItem.class),
                        Character.valueOf('Z'),
                        new ItemStack(Items.STICK, 1, 0) });

        rockDropperRecipe = new ShapedOreRecipe(ItemManager.getItemStack(BlockRockDropper.class),
                new Object[] {
                        "XXX",
                        " RX",
                        "XXX",
                        Character.valueOf('X'),
                        new ItemStack(ModBlocks.livingrock),
                        Character.valueOf('R'),
                        new ItemStack(Items.REDSTONE) });

        extraRecipe = new ShapedOreRecipe(ItemManager.getItemStack(ItemExtra.class),
                new Object[] {
                        "XXX",
                        "DXD",
                        "XXX",
                        Character.valueOf('X'),
                        new ItemStack(ModBlocks.livingrock),
                        Character.valueOf('D'),
                        new ItemStack(Items.DIAMOND) });

        ItemStack bindSwordFlower = getFlower(SubTileBindSword.NAME);
        bindSwordFlowerRecipe = BotaniaAPI.registerPetalRecipe(bindSwordFlower,
                "petalGreen", "petalGreen", "petalBlack",
                ItemManager.getItemStack(RuneItem.class),
                "redstoneRoot", "elvenPixieDust");

        GameRegistry.addRecipe(shotSwordRecipe);
        GameRegistry.addRecipe(standCoreRecipe);
        GameRegistry.addRecipe(rockDropperRecipe);
        GameRegistry.addRecipe(extraRecipe);
        GameRegistry.addRecipe(homingRecipe);

        ItemStack createManaFlower = getFlower(SubTileCreateManaFluid.NAME);
        createManaFlowerRecipe = BotaniaAPI.registerPetalRecipe(createManaFlower,
                "petalBlue", "petalBlue", "petalCyan", "petalCyan",
                "runeEnvyB", "runePrideB",
                "redstoneRoot", "elvenPixieDust");

        ItemStack fallingBlockFlower = getFlower(SubTileFallingBlock.NAME);
        fallingBlockRecipe = BotaniaAPI.registerPetalRecipe(fallingBlockFlower,
                "petalLime", "petalLime", "petalYellow",
                "runeAutumnB", "redstoneRoot");
    }

    /**
     * ボタニアの花の取得。
     * @param name 花の名前。
     * @return ボタニアの花。
     */
    public static ItemStack getFlower(String name) {
        return ItemBlockSpecialFlower.ofType(name);
    }
}
