package com.github.highd120.block.injection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.highd120.item.RuneItem;
import com.github.highd120.util.item.ItemManager;

import lombok.Builder;
import lombok.Value;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.ModItems;

/**
 * 注入レシピのマネージャー。
 * @author hdgam
 */
public class InjectionRecipe {

    @Builder
    @Value
    public static class Input {
        private ItemStack main;
        private List<ItemStack> injectionList;

        /**
         * JEIのIngredientを作成。
         * @return Ingredient。
         */
        public List<List<ItemStack>> createIngredient() {
            List<List<ItemStack>> ingredient = new ArrayList<>();
            ingredient.add(Collections.singletonList(main));
            ingredient.add(injectionList);
            return ingredient;
        }
    }

    @Builder
    @Value
    public static class Data {
        private Input input;
        private ItemStack output;
        private int useMana;

        /**
         * Ingredientからレシピの作成。
         * @return レシピ。
         */
        public static Data parseIngredient(IIngredients ingredients) {
            ItemStack main = ingredients.getInputs(ItemStack.class).get(0).get(0);
            List<ItemStack> injectionList = ingredients.getInputs(ItemStack.class).get(1);
            ItemStack output = ingredients.getOutputs(ItemStack.class).get(0);
            return new Data(new Input(main, injectionList), output, 0);

        }
    }

    public static final List<InjectionRecipe.Data> recipes = new ArrayList<>();

    /**
     * 注入レシピの作成。
     * @param main 注入するアイテム。
     * @param output 注入後のアイテム。
     * @param injections 注入の材料。
     * @return レシピ。
     */
    public static Data createRecipe(ItemStack main, ItemStack output, int useMana,
            ItemStack... injections) {
        List<ItemStack> injectionList = Arrays.asList(injections);
        Input input = new Input(main, injectionList);
        return new Data(input, output, useMana);
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

        recipes.add(createRecipe(new ItemStack(ModItems.dice),
                new ItemStack(ModItems.flugelEye),
                40000,
                new ItemStack(Blocks.DIAMOND_BLOCK), new ItemStack(ModItems.rainbowRod),
                new ItemStack(Items.ENDER_EYE), ItemManager.getItemStack(RuneItem.class)));
    }
}
