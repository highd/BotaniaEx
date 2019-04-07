package com.github.highd120.list;

import com.github.highd120.block.BlockStand;
import com.github.highd120.block.SubTileBindSword;
import com.github.highd120.block.SubTileCreateManaFluid;
import com.github.highd120.block.SubTileFallingBlock;
import com.github.highd120.block.injection.BlockInjection;
import com.github.highd120.item.RuneItem;
import com.github.highd120.item.ShotSwordItem;
import com.github.highd120.util.item.ItemManager;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.api.subtile.SubTileEntity;

/**
 * レシピのリスト。
 * @author hdgam
 */
public class RecipeList {
    public static RecipeRuneAltar runeRecipe;
    public static RecipeRuneAltar injectionCoreRecipe;
    public static ShapedOreRecipe standCoreRecipe;
    public static ShapedOreRecipe shotSwordRecipe;
    public static RecipePetals bindSwordFlowerRecipe;
    public static RecipePetals createManaFlowerRecipe;
    public static RecipePetals fallingBlockRecipe;

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

        @SuppressWarnings("deprecation")
        Item shimmerrock = GameData.getItemRegistry()
                .getObject(new ResourceLocation("botania", "shimmerrock"));
        injectionCoreRecipe = BotaniaAPI.registerRuneAltarRecipe(
                ItemManager.getItemStack(BlockInjection.class),
                1000,
                new ItemStack(shimmerrock), "livingwood", new ItemStack(shimmerrock), "livingwood",
                new ItemStack(shimmerrock), "livingwood", new ItemStack(shimmerrock), "livingwood",
                ItemManager.getItemStack(RuneItem.class));

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

        ItemStack bindSwordFlower = getFlower(SubTileBindSword.NAME);
        bindSwordFlowerRecipe = BotaniaAPI.registerPetalRecipe(bindSwordFlower,
                "petalGreen", "petalGreen", "petalBlack",
                ItemManager.getItemStack(RuneItem.class),
                "redstoneRoot", "elvenPixieDust");

        GameRegistry.addRecipe(shotSwordRecipe);
        GameRegistry.addRecipe(standCoreRecipe);

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
        @SuppressWarnings("deprecation")
        Item flower = GameData.getItemRegistry()
                .getObject(new ResourceLocation("botania", "specialFlower"));
        ItemStack flowerStack = new ItemStack(flower);
        NBTTagCompound compound = flowerStack.getTagCompound();
        if (compound == null) {
            compound = new NBTTagCompound();
            flowerStack.setTagCompound(compound);
        }
        compound.setString(SubTileEntity.TAG_TYPE, name);
        return flowerStack;
    }
}
