package com.github.highd120;

import com.github.highd120.block.SubTileBindSword;
import com.github.highd120.item.ItemList;

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
    public static ShapedOreRecipe shotSwordRecipe;
    public static RecipePetals bindSwordFlowerRecipe;

    /**
     * レシピの登録。
     */
    public static void init() {
        runeRecipe = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ItemList.runeItem), 5000,
                "runeWaterB", "runeFireB", "runeEarthB", "runeAirB",
                "runeSpringB", "runeSummerB", "runeAutumnB", "runeWinterB",
                "runeManaB", "runeLustB", "runeGluttonyB", "runeGreedB",
                "runeSlothB", "runeWrathB", "runeEnvyB", "runePrideB");

        shotSwordRecipe = new ShapedOreRecipe(new ItemStack(ItemList.shotSwordItem, 1, 0),
                new Object[] {
                        " X ",
                        "ZYZ",
                        "   ",
                        Character.valueOf('X'),
                        new ItemStack(Items.DIAMOND_SWORD, 1, 0),
                        Character.valueOf('Y'),
                        new ItemStack(ItemList.runeItem, 1, 0),
                        Character.valueOf('Z'),
                        new ItemStack(Items.STICK, 1, 0) });

        ItemStack bindSwordFlower = getFlower(SubTileBindSword.NAME);
        bindSwordFlowerRecipe = BotaniaAPI.registerPetalRecipe(bindSwordFlower,
                "petalGreen", "petalGreen", "petalBlack",
                "runeEnvyB", "runePrideB",
                "redstoneRoot", "elvenPixieDust");

        GameRegistry.addRecipe(shotSwordRecipe);
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
