package com.github.highd120.jei;

import com.github.highd120.block.injection.InjectionRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

public class InjectionRecipeWrapper extends BlankRecipeWrapper {
    private final InjectionRecipe.Data recipe;

    public InjectionRecipeWrapper(InjectionRecipe.Data recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, recipe.getInput().createIngredient());
        ingredients.setOutput(ItemStack.class, recipe.getOutput());
    }

}
