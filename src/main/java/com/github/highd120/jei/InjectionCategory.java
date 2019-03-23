package com.github.highd120.jei;

import java.util.stream.IntStream;

import com.github.highd120.BotaniaExMain;
import com.github.highd120.block.injection.InjectionRecipe.Data;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;

public class InjectionCategory extends BlankRecipeCategory<InjectionRecipeWrapper> {
    private final IDrawable background;
    private final IDrawable slotDrawable;
    private static final int outputSlotX = 80;
    private static final int outputSlotY = 1;
    private static final int RADIUS = 30;
    public static final String UID = BotaniaExMain.MOD_ID + ".Injection";

    /**
            * コンストラクター。
     * @param guiHelper GUIのヘルパー。
     */
    public InjectionCategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(150, 110);
        slotDrawable = guiHelper.getSlotDrawable();
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return "Injection";
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        slotDrawable.draw(minecraft, outputSlotX, outputSlotY);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, InjectionRecipeWrapper recipeWrapper,
            IIngredients ingredients) {
        recipeWrapper.getIngredients(ingredients);
        final Data recipe = Data.parseIngredient(ingredients);
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 30, 50);
        itemStacks.set(0, recipe.getInput().getMain());
        final int inputSize = recipe.getInput().getInjectionList().size();
        IntStream.range(1, inputSize + 1).forEach(v -> {
            int x = (int) Math.cos(Math.PI * 2 / inputSize * v) * RADIUS + 30;
            int y = (int) Math.sin(Math.PI * 2 / inputSize * v) * RADIUS + 50;
            itemStacks.init(v, true, x, y);
            itemStacks.set(v, recipe.getInput().getInjectionList().get(v - 1));
        });
        itemStacks.init(inputSize + 1, false, 90, 50);
        itemStacks.set(inputSize + 1, recipe.getOutput());
    }
}
