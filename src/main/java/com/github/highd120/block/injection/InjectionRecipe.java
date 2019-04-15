package com.github.highd120.block.injection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.highd120.block.injection.InjectionRecipeData.Input;
import com.github.highd120.item.ItemExtra;
import com.github.highd120.item.RuneItem;
import com.github.highd120.util.item.ItemManager;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
                new ItemStack(Items.GOLDEN_APPLE), ItemManager.getItemStack(RuneItem.class),
                new ItemStack(ModItems.manaGun)));

        recipes.add(createRecipe(new ItemStack(ModItems.dice),
                new ItemStack(ModItems.lokiRing),
                40000,
                new ItemStack(Blocks.DIAMOND_BLOCK), new ItemStack(ModItems.auraRing),
                new ItemStack(Items.GOLDEN_APPLE), ItemManager.getItemStack(RuneItem.class)));

        recipes.add(createRecipe(new ItemStack(ModItems.dice),
                new ItemStack(ModItems.thorRing),
                40000,
                new ItemStack(Blocks.DIAMOND_BLOCK), new ItemStack(Items.DIAMOND_PICKAXE),
                new ItemStack(Items.BLAZE_POWDER), ItemManager.getItemStack(RuneItem.class)));

        recipes.add(createRecipe(new ItemStack(ModItems.dice),
                new ItemStack(ModItems.infiniteFruit),
                40000,
                new ItemStack(Blocks.DIAMOND_BLOCK), new ItemStack(ModItems.manaTablet),
                new ItemStack(Items.GOLDEN_APPLE), ItemManager.getItemStack(RuneItem.class)));

        recipes.add(createRecipe(new ItemStack(ModItems.dice),
                new ItemStack(ModItems.kingKey),
                40000,
                new ItemStack(Blocks.DIAMOND_BLOCK), new ItemStack(ModItems.crystalBow),
                new ItemStack(Items.ENDER_EYE), ItemManager.getItemStack(RuneItem.class)));

        recipes.add(ItemExtraInjection.createRecipe(ItemExtra.Type.FIRE, 40000,
                new ItemStack(Items.MAGMA_CREAM)));

        recipes.add(ItemExtraInjection.createRecipe(ItemExtra.Type.WATER, 40000,
                new ItemStack(Items.WATER_BUCKET)));

        recipes.add(ItemExtraInjection.createRecipe(ItemExtra.Type.DIRT, 40000,
                new ItemStack(Items.APPLE)));

        recipes.add(ItemExtraInjection.createRecipe(ItemExtra.Type.WIND, 40000,
                new ItemStack(Items.ARROW)));
    }
}
