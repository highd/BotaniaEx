package com.github.highd120.jei;

import com.github.highd120.block.injection.InjectionRecipe;
import com.github.highd120.item.ItemList;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class InjectionPlugin extends BlankModPlugin {
    @Override
    public void register(IModRegistry registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        registry.addRecipeCategories(new InjectionCategory(jeiHelpers.getGuiHelper()));
        registry.addRecipeHandlers(new InjectionHandler());
        registry.addRecipes(InjectionRecipe.recipes);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ItemList.shotSwordItem),
                InjectionCategory.UID);
    }
}
