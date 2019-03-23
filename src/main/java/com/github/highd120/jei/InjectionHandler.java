package com.github.highd120.jei;

import com.github.highd120.block.injection.InjectionRecipe.Data;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class InjectionHandler implements IRecipeHandler<Data> {

    @Override
    public Class<Data> getRecipeClass() {
        return Data.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return InjectionCategory.UID;
    }

    @Override
    public String getRecipeCategoryUid(Data recipe) {
        return getRecipeCategoryUid();
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(Data recipe) {
        return new InjectionRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(Data recipe) {
        return true;
    }

}
