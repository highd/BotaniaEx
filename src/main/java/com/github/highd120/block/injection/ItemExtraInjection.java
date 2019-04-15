package com.github.highd120.block.injection;

import java.util.Arrays;
import java.util.List;

import com.github.highd120.item.ItemExtra;
import com.github.highd120.item.ItemExtra.Type;
import com.github.highd120.util.item.ItemManager;

import net.minecraft.item.ItemStack;

public class ItemExtraInjection extends InjectionRecipeData {
    private Type type;

    public ItemExtraInjection(Input input, ItemStack output, int useMana, Type type) {
        super(input, output, useMana);
        this.type = type;
    }

    @Override
    public boolean checkRecipe(List<ItemStack> itemList, ItemStack injection) {
        List<String> itemNameList = getItemNameList(itemList);
        List<String> recipe = getItemNameList(input.getInjectionList());
        if (!(injection.getItem() instanceof ItemExtra)) {
            return false;
        }
        List<Type> typeList = ItemExtra.getType(injection);
        return itemNameList.equals(recipe) && !typeList.contains(type);
    }

    @Override
    public ItemStack craft(List<ItemStack> itemList, ItemStack injection) {
        return ItemExtra.setType(injection, type);
    }

    /**
     * レシピの作成。
     * @param type 付与する属性。
     * @param useMana 使用するマナ。
     * @param injections 注入するアイテム。
     * @return レシピ。
     */
    public static InjectionRecipeData createRecipe(Type type, int useMana,
            ItemStack... injections) {
        List<ItemStack> injectionList = Arrays.asList(injections);
        ItemStack main = ItemManager.getItemStack(ItemExtra.class);
        ItemStack output = ItemExtra.setType(ItemManager.getItemStack(ItemExtra.class), type);
        Input input = new Input(main, injectionList);
        return new ItemExtraInjection(input, output, useMana, type);
    }
}
