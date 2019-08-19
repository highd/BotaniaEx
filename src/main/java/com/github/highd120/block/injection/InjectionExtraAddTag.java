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

public class InjectionExtraAddTag extends InjectionAddTag {

    public InjectionExtraAddTag(Input input, ItemStack output, int useMana, String tag, Type type,
            int minLv) {
        super(input, output, useMana, tag, type, minLv);
    }

    @Override
    public ItemStack craft(List<ItemStack> itemList, ItemStack injection) {
        ItemStack result = output.copy();
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
     * @param output 作成されるアイテム。
     * @return レシピ。
     */
    public static InjectionExtraAddTag createRecipe(String tag, int useMana, Type type, int minLv,
            ItemStack enchant, ItemStack output, ItemStack... injections) {
        List<ItemStack> injectionList = new ArrayList<>(Arrays.asList(injections));
        ItemStack extra = ItemManager.getItemStack(ItemExtra.class);
        int[] propertys = { 0b1, 0b11, 0b111, 0b1111 };
        ItemExtra.setProperty(extra, propertys[minLv]);
        injectionList.add(extra);
        NbtTagUtil.getCompound(output).setString(tag, "");
        Input input = new Input(enchant, injectionList);
        return new InjectionExtraAddTag(input, output, useMana, tag, type, minLv);
    }
}
