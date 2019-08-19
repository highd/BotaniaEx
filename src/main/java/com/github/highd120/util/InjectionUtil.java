package com.github.highd120.util;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InjectionUtil {
    /**
     * エンチャントのテキストの追加。
     * @param stack アイテムスタック。
     * @param tooltip ツールチップ。
     * @param list タグのリスト。
     */
    public static void addInformation(ItemStack stack, List<String> tooltip, String[] list) {
        NBTTagCompound compound = NbtTagUtil.getCompound(stack);
        Arrays.stream(list)
                .filter(tag -> compound.hasKey(tag))
                .forEach(tag -> tooltip.add(I18n.format("botaniaex.injection." + tag)));
    }
}
