package com.github.highd120.jei;

import com.github.highd120.block.injection.BlockInjection;
import com.github.highd120.block.injection.InjectionRecipe;
import com.github.highd120.util.item.ItemManager;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class InjectionPlugin extends BlankModPlugin {
    @Override
    public void register(IModRegistry registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        registry.addRecipeCategories(new InjectionCategory(jeiHelpers.getGuiHelper()));
        registry.addRecipeHandlers(new InjectionHandler());
        registry.addRecipes(InjectionRecipe.recipes);
        registry.addRecipeCategoryCraftingItem(ItemManager.getItemStack(BlockInjection.class),
                InjectionCategory.UID);
    }
}
