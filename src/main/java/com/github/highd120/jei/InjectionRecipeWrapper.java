package com.github.highd120.jei;

import javax.annotation.Nonnull;

import com.github.highd120.block.injection.InjectionRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.tile.mana.TilePool;

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

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight,
            int mouseX, int mouseY) {
        GlStateManager.enableAlpha();
        HUDHandler.renderManaBar(28, 113, 0x0000FF, 0.75F, recipe.getUseMana(),
                TilePool.MAX_MANA / 10);
        GlStateManager.disableAlpha();
    }

}
