package com.github.highd120.jei;

import java.awt.Point;

import javax.annotation.Nonnull;

import com.github.highd120.BotaniaExMain;
import com.github.highd120.block.injection.InjectionRecipeData;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class InjectionCategory extends BlankRecipeCategory<InjectionRecipeWrapper> {
    private final IDrawable background;
    public static final String UID = BotaniaExMain.MOD_ID + ".Injection";
    private final IDrawable overlay;

    /**
     * コンストラクター。
     * @param guiHelper GUIのヘルパー。
     */
    public InjectionCategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(150, 110);
        overlay = guiHelper.createDrawable(
                new ResourceLocation("botania", "textures/gui/petalOverlay.png"), 0, 0, 150, 110);
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
    public void drawExtras(@Nonnull Minecraft minecraft) {
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        overlay.draw(minecraft);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, InjectionRecipeWrapper recipeWrapper,
            IIngredients ingredients) {
        recipeWrapper.getIngredients(ingredients);
        final InjectionRecipeData recipe = InjectionRecipeData.parseIngredient(ingredients);
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 64, 52);
        itemStacks.set(0, recipe.getMain());
        final int inputSize = recipe.getInjectionList().size();
        double angleBetweenEach = 360.0 / inputSize;
        Point point = new Point(64, 20);
        Point center = new Point(64, 52);
        int index = 1;
        for (ItemStack item : recipe.getInjectionList()) {
            itemStacks.init(index, true, point.x, point.y);
            itemStacks.set(index, item);
            index += 1;
            point = rotatePointAbout(point, center, angleBetweenEach);
        }
        itemStacks.init(inputSize + 1, false, 103, 17);
        itemStacks.set(inputSize + 1, recipe.getOutput());
    }

    private Point rotatePointAbout(Point in, Point about, double degrees) {
        double rad = degrees * Math.PI / 180.0;
        double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
        double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
        return new Point((int) newX, (int) newY);
    }
}
