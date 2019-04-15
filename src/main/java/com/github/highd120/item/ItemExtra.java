package com.github.highd120.item;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.highd120.util.NbtTagUtil;
import com.github.highd120.util.item.ItemRegister;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

@ItemRegister(name = "extra")
public class ItemExtra extends Item {
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
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn,
            EntityPlayer playerIn, EnumHand hand) {
        int data = NbtTagUtil.getInterger("type", itemStackIn).orElse(0);

        NbtTagUtil.setInterger("type", itemStackIn, (data + 1) % 16);
        playerIn.addChatMessage(new TextComponentString(Integer.toString(data)));

        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
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
        int data = NbtTagUtil.getInterger("type", stack)
                .map(n -> n | mask)
                .orElse(0);
        NbtTagUtil.setInterger("type", result, data);
        return result;
    }

}
