package com.github.highd120.block.injection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.highd120.util.NbtTagUtil;

import net.minecraft.item.ItemStack;

public class InjectionRemoveTag extends InjectionRecipeData {
    private String tag;

    /**
     * コンストラクター。
     * @param input メインのアイテム。
     * @param output 出力するアイテム。
     * @param useMana 使用するマナ。
     * @param tag セットするタグ。
     */
    public InjectionRemoveTag(Input input, ItemStack output, int useMana, String tag) {
        super(input, output, useMana);
        this.tag = tag;
    }

    @Override
    public boolean checkRecipe(List<ItemStack> itemList, ItemStack injection) {
        boolean isValid = super.checkRecipe(itemList, injection);
        return isValid && NbtTagUtil.getCompound(injection).hasKey(tag);
    }

    @Override
    public ItemStack craft(List<ItemStack> itemList, ItemStack injection) {
        ItemStack result = injection.copy();
        NbtTagUtil.removeTag(tag, result);
        return result;
    }

    /**
     * レシピの作成。
     * @param tag 付与するタグ。
     * @param useMana 使用するマナ。
     * @param injections 注入するアイテム。
     * @return レシピ。
     */
    public static InjectionRemoveTag createRecipe(String tag, int useMana, ItemStack enchant,
            ItemStack... injections) {
        List<ItemStack> injectionList = new ArrayList<>(Arrays.asList(injections));
        ItemStack main = enchant.copy();
        NbtTagUtil.getCompound(main).setInteger(tag, 1);
        Input input = new Input(main, injectionList);
        return new InjectionRemoveTag(input, enchant, useMana, tag);
    }

}
