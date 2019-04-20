package com.github.highd120.item;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.highd120.util.NbtTagUtil;
import com.github.highd120.util.item.ItemManager;
import com.github.highd120.util.item.ItemRegister;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@ItemRegister(name = "extra")
public class ItemExtra extends ItemBase {
    public static enum Type {
        WIND("wind"), FIRE("fire"), DIRT("dirt"), WATER("water");

        private String name;

        Type(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }

    private static Type[] typeList = Type.values();

    public ItemExtra() {
        addPropertyOverride(new ResourceLocation("botaniaex", "type"),
                (stack, worldIn, entityIn) -> getProperty(stack));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip,
            boolean advanced) {
        String text = Arrays.stream(typeList)
                .filter(type -> isHasType(stack, type))
                .map(type -> I18n.format("botaniaex." + type.name))
                .collect(Collectors.joining(","));
        tooltip.add(text);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        super.getSubItems(itemIn, tab, subItems);
        subItems.add(create(0b0001));
        subItems.add(create(0b0010));
        subItems.add(create(0b0100));
        subItems.add(create(0b1000));
        subItems.add(create(0b0011));
        subItems.add(create(0b0111));
        subItems.add(create(0b1111));
    }

    /**
     * 属性を持っているか。
     * @param stack アイテムスタック。
     * @param type 属性。
     * @return  属性を持っているか。
     */
    public static boolean isHasType(ItemStack stack, Type type) {
        int mask = 1 << type.ordinal();
        int data = NbtTagUtil.getInterger("type", stack).orElse(0);
        return (data & mask) >> type.ordinal() == 1;
    }

    public static int getProperty(ItemStack stack) {
        return NbtTagUtil.getInterger("type", stack).orElse(0);
    }

    public static void setProperty(ItemStack stack, int data) {
        NbtTagUtil.setInterger("type", stack, data);
    }

    /**
     * アイテムスタックの作成。
     * @param data 内部データ。
     * @return アイテムスタック。
     */
    public static ItemStack create(int data) {
        ItemStack stack = ItemManager.getItemStack(ItemExtra.class);
        setProperty(stack, data);
        return stack;
    }

    /**
     * 属性の取得。
     * @param stack アイテム。
     * @return 属性。
     */
    public static List<Type> getType(ItemStack stack) {
        return Arrays.stream(typeList)
                .filter(type -> isHasType(stack, type))
                .collect(Collectors.toList());
    }

    /**
     * 属性のセット。
     * @param stack アイテム。
     * @param type 属性。
     * @return セットしたアイテム。
     */
    public static ItemStack setType(ItemStack stack, Type type) {
        ItemStack result = stack.copy();
        int mask = 1 << type.ordinal();
        int data = NbtTagUtil.getInterger("type", stack).orElse(0);
        NbtTagUtil.setInterger("type", result, data | mask);
        return result;
    }

}
