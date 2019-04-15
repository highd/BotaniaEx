package com.github.highd120.block.injection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;

@AllArgsConstructor
public class InjectionRecipeData {
    @Builder
    @Value
    public static class Input {
        private ItemStack main;
        private List<ItemStack> injectionList;
    }

    protected Input input;
    protected ItemStack output;
    protected int useMana;

    /**
     * Ingredientからレシピの作成。
     * @return レシピ。
     */
    public static InjectionRecipeData parseIngredient(IIngredients ingredients) {
        ItemStack main = ingredients.getInputs(ItemStack.class).get(0).get(0);
        List<ItemStack> injectionList = ingredients.getInputs(ItemStack.class).get(1);
        ItemStack output = ingredients.getOutputs(ItemStack.class).get(0);
        return new InjectionRecipeData(new Input(main, injectionList), output, 0);

    }

    protected String getItemName(ItemStack stack) {
        return stack.getItem().getRegistryName().toString();
    }

    protected List<String> getItemNameList(List<ItemStack> itemList) {
        return itemList.stream()
                .map(item -> getItemName(item))
                .filter(name -> name != null)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * レシピに合致しているか。
     * @param itemList アイテムのリスト。
     * @param injection 注入するアイテム。
     * @return
     */
    public boolean checkRecipe(List<ItemStack> itemList, ItemStack injection) {
        List<String> itemNameList = getItemNameList(itemList);
        List<String> recipe = getItemNameList(input.getInjectionList());
        return recipe.equals(itemNameList)
                && getItemName(input.main).equals(getItemName(injection));
    }

    public ItemStack craft(List<ItemStack> itemList, ItemStack injection) {
        return output.copy();
    }

    public int getUseMana() {
        return useMana;
    }

    public ItemStack getOutput() {
        return output;
    }

    public List<ItemStack> getInjectionList() {
        return input.getInjectionList();
    }

    public ItemStack getMain() {
        return input.getMain();
    }

    /**
     * JEIのIngredientを作成。
     * @return Ingredient。
     */
    public List<List<ItemStack>> createIngredient() {
        List<List<ItemStack>> ingredient = new ArrayList<>();
        ingredient.add(Collections.singletonList(input.getMain()));
        ingredient.add(input.getInjectionList());
        return ingredient;
    }

}
