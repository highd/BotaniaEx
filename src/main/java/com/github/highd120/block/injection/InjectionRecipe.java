package com.github.highd120.block.injection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.highd120.block.injection.InjectionRecipeData.Input;
import com.github.highd120.item.ItemExtra;
import com.github.highd120.item.ItemExtraCobbleRod;
import com.github.highd120.item.ItemInjectionResource;
import com.github.highd120.item.ItemRune;
import com.github.highd120.item.ItemShotSword;
import com.github.highd120.util.item.ItemManager;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

/**
 * 注入レシピのマネージャー。
 * @author hdgam
 */
public class InjectionRecipe {

    public static final List<InjectionRecipeData> recipes = new ArrayList<>();

    /**
     * 注入レシピの作成。
     * @param main 注入するアイテム。
     * @param output 注入後のアイテム。
     * @param injections 注入の材料。
     * @return レシピ。
     */
    public static InjectionRecipeData createRecipe(ItemStack main, ItemStack output, int useMana,
            ItemStack... injections) {
        List<ItemStack> injectionList = Arrays.asList(injections);
        Input input = new Input(main, injectionList);
        return new InjectionRecipeData(input, output, useMana);
    }

    /**
     * レシピの初期化。
     */
    public static void init() {
        recipes.add(createRecipe(new ItemStack(Items.BUCKET),
                new ItemStack(Items.WATER_BUCKET),
                1000,
                new ItemStack(Blocks.LEAVES), new ItemStack(Blocks.LEAVES),
                new ItemStack(Blocks.LEAVES), new ItemStack(Blocks.LEAVES)));
        recipes.add(createRecipe(new ItemStack(Items.BUCKET),
                new ItemStack(Items.LAVA_BUCKET),
                1000,
                new ItemStack(Blocks.MAGMA), new ItemStack(Blocks.MAGMA),
                new ItemStack(Blocks.MAGMA), new ItemStack(Blocks.MAGMA)));

        recipes.add(createRecipe(new ItemStack(ModItems.dice),
                new ItemStack(ModItems.odinRing),
                40000,
                new ItemStack(Blocks.DIAMOND_BLOCK), new ItemStack(ModItems.terraSword),
                new ItemStack(Items.GOLDEN_APPLE), ItemManager.getItemStack(ItemRune.class),
                new ItemStack(ModBlocks.storage, 1, 4)));

        recipes.add(createRecipe(new ItemStack(ModItems.dice),
                new ItemStack(ModItems.lokiRing),
                40000,
                new ItemStack(Blocks.DIAMOND_BLOCK), new ItemStack(ModItems.auraRing),
                new ItemStack(Items.GOLDEN_APPLE), ItemManager.getItemStack(ItemRune.class)));

        recipes.add(createRecipe(new ItemStack(ModItems.dice),
                new ItemStack(ModItems.thorRing),
                40000,
                new ItemStack(Blocks.DIAMOND_BLOCK), new ItemStack(Items.DIAMOND_PICKAXE),
                new ItemStack(Items.BLAZE_POWDER), ItemManager.getItemStack(ItemRune.class)));

        recipes.add(createRecipe(new ItemStack(ModItems.dice),
                new ItemStack(ModItems.infiniteFruit),
                40000,
                new ItemStack(Blocks.DIAMOND_BLOCK), new ItemStack(ModItems.manaTablet),
                new ItemStack(Items.GOLDEN_APPLE), ItemManager.getItemStack(ItemRune.class)));

        recipes.add(createRecipe(new ItemStack(ModItems.dice),
                new ItemStack(ModItems.kingKey),
                40000,
                new ItemStack(Blocks.DIAMOND_BLOCK), new ItemStack(ModItems.crystalBow),
                new ItemStack(Items.ENDER_EYE), ItemManager.getItemStack(ItemRune.class)));

        recipes.add(createRecipe(new ItemStack(ModItems.dice),
                new ItemStack(ModItems.flugelEye),
                40000,
                new ItemStack(Blocks.DIAMOND_BLOCK), new ItemStack(ModItems.rainbowRod),
                new ItemStack(Items.ENDER_EYE), ItemManager.getItemStack(ItemRune.class)));

        recipes.add(ItemExtraInjection.createRecipe(ItemExtra.Type.FIRE, 40000,
                new ItemStack(ModItems.rune, 1, 1),
                new ItemStack(Items.SLIME_BALL),
                new ItemStack(ModItems.manaResource, 1, 14),
                new ItemStack(ModItems.manaResource, 1, 12),
                new ItemStack(ModItems.manaResource, 1, 15)));

        recipes.add(ItemExtraInjection.createRecipe(ItemExtra.Type.WATER, 40000,
                new ItemStack(ModItems.rune, 1, 0),
                new ItemStack(Items.SLIME_BALL),
                new ItemStack(ModItems.manaResource, 1, 14),
                new ItemStack(ModItems.manaResource, 1, 12),
                new ItemStack(ModItems.manaResource, 1, 15)));

        recipes.add(ItemExtraInjection.createRecipe(ItemExtra.Type.DIRT, 40000,
                new ItemStack(ModItems.rune, 1, 2),
                new ItemStack(Items.SLIME_BALL),
                new ItemStack(ModItems.manaResource, 1, 14),
                new ItemStack(ModItems.manaResource, 1, 12),
                new ItemStack(ModItems.manaResource, 1, 15)));

        recipes.add(ItemExtraInjection.createRecipe(ItemExtra.Type.WIND, 40000,
                new ItemStack(ModItems.rune, 1, 3),
                new ItemStack(Items.SLIME_BALL),
                new ItemStack(ModItems.manaResource, 1, 14),
                new ItemStack(ModItems.manaResource, 1, 12),
                new ItemStack(ModItems.manaResource, 1, 15)));

        recipes.add(InjectionAddTag.createRecipe(ItemShotSword.HOMING_TAG, 40000,
                InjectionAddTag.Type.OVER, 3,
                ItemManager.getItemStack(ItemShotSword.class),
                ItemManager.getItemStack(ItemInjectionResource.class, 0),
                new ItemStack(Items.ENDER_EYE),
                new ItemStack(ModItems.manaResource, 1, 14),
                new ItemStack(ModItems.manaResource, 1, 12),
                new ItemStack(ModItems.manaResource, 1, 15)));

        recipes.add(InjectionRemoveTag.createRecipe(ItemShotSword.HOMING_TAG, 400,
                ItemManager.getItemStack(ItemShotSword.class),
                ItemManager.getItemStack(ItemInjectionResource.class, 0),
                new ItemStack(ModItems.manaResource, 1, 15)));

        recipes.add(InjectionExtraAddTag.createRecipe("remove", 40000,
                InjectionAddTag.Type.OVER, 1,
                new ItemStack(ModItems.cobbleRod),
                ItemManager.getItemStack(ItemExtraCobbleRod.class, 0),
                new ItemStack(Items.ENDER_EYE),
                new ItemStack(ModItems.manaResource, 1, 14),
                new ItemStack(ModItems.manaResource, 1, 12),
                new ItemStack(ModItems.manaResource, 1, 15)));
    }
}
