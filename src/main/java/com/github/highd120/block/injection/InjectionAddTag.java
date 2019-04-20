package com.github.highd120.block.injection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.highd120.item.ItemExtra;
import com.github.highd120.util.CollectionUtil;
import com.github.highd120.util.NbtTagUtil;
import com.github.highd120.util.item.ItemManager;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InjectionAddTag extends InjectionRecipeData {
    private String tag;
    private Type type;
    private int minLv;

    public enum Type {
        OVER, LANK
    }

    /**
     * コンストラクター。
     * @param input メインのアイテム。
     * @param output 出力するアイテム。
     * @param useMana 使用するマナ。
     * @param tag セットするタグ。
     * @param type クラフトの種類。
     * @param minLv 最低レベル。
     */
    public InjectionAddTag(Input input, ItemStack output, int useMana, String tag, Type type,
            int minLv) {
        super(input, output, useMana);
        this.tag = tag;
        this.type = type;
        this.minLv = minLv;
    }

    @Override
    public boolean checkRecipe(List<ItemStack> itemList, ItemStack injection) {
        boolean isValid = super.checkRecipe(itemList, injection);
        Item extra = ItemManager.getItem(ItemExtra.class);
        int lv = CollectionUtil.findIf(itemList, stack -> stack.getItem() == extra)
                .map(stack -> ItemExtra.getType(stack).size())
                .orElse(-1);
        return isValid && (!NbtTagUtil.getCompound(injection).hasKey(tag)) && lv > minLv;
    }

    @Override
    public ItemStack craft(List<ItemStack> itemList, ItemStack injection) {
        ItemStack result = injection.copy();
        Item extra = ItemManager.getItem(ItemExtra.class);
        int lv = CollectionUtil.findIf(itemList, stack -> stack.getItem() == extra)
                .map(stack -> ItemExtra.getType(stack).size())
                .orElse(-1);
        if (type == Type.OVER) {
            NbtTagUtil.getCompound(result).setInteger(tag, 0);
        } else {
            NbtTagUtil.getCompound(result).setInteger(tag, lv);
        }
        return result;
    }

    /**
     * レシピの作成。
     * @param tag 付与するタグ。
     * @param useMana 使用するマナ。
     * @param injections 注入するアイテム。
     * @return レシピ。
     */
    public static InjectionAddTag createRecipe(String tag, int useMana, Type type, int minLv,
            ItemStack enchant, ItemStack... injections) {
        List<ItemStack> injectionList = new ArrayList<>(Arrays.asList(injections));
        ItemStack extra = ItemManager.getItemStack(ItemExtra.class);
        int[] propertys = { 0b1, 0b11, 0b111, 0b1111 };
        ItemExtra.setProperty(extra, propertys[minLv]);
        injectionList.add(extra);
        ItemStack main = enchant.copy();
        NbtTagUtil.getCompound(enchant).setString(tag, "");
        Input input = new Input(main, injectionList);
        return new InjectionAddTag(input, enchant, useMana, tag, type, minLv);
    }

}
