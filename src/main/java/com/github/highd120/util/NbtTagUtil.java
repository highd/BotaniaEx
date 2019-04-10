package com.github.highd120.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * NBTタグのユーティリティ。
 * @author hdgam
 */
public class NbtTagUtil {
    /**
     * Compoundの取得。
     * @param key キー。
     * @param stack アイテム。
     * @return Compound。
     */
    public static NBTTagCompound getCompound(String key, ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) {
            return new NBTTagCompound();
        }
        if (!compound.hasKey(key)) {
            return new NBTTagCompound();
        }
        return compound.getCompoundTag(key);
    }

    /**
     * NBTタグから文字列の取得。
     * @param key キー。
     * @param stack アイテム。
     * @return 文字列
     */
    public static Optional<String> getString(String key, ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) {
            return Optional.empty();
        }
        if (!compound.hasKey(key)) {
            return Optional.empty();
        }
        return Optional.ofNullable(compound.getString(key));
    }

    /**
     * Compoundのセット。
     * @param key キー。
     * @param stack アイテム。
     * @param tag Compound。
     */
    public static void setCompound(String key, ItemStack stack, NBTTagCompound tag) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setTag(key, tag);
    }

    /**
     * タグの削除。
     * @param key キー。
     * @param stack アイテム。
     */
    public static void removeTag(String key, ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null) {
            compound.removeTag(key);
        }
    }

    /**
     * 内部アイテムの取得。
     * @param key キー。
     * @param stack 外部となるアイテム。
     * @return 内部アイテム。
     */
    public static Optional<ItemStack> getInnerItem(String key, ItemStack stack) {
        NBTTagCompound compound = getCompound(key, stack);
        ItemStack inner = ItemStack.loadItemStackFromNBT(compound);
        return Optional.ofNullable(inner);
    }

    /**
     * 内部アイテムのセット。
     * @param key キー。
     * @param outer 外部となるアイテム。
     * @param inner 内部アイテム。
     */
    public static void setInnerItem(String key, ItemStack outer, ItemStack inner) {
        NBTTagCompound child = new NBTTagCompound();
        inner.writeToNBT(child);
        setCompound(key, outer, child);
    }

    /**
     * Vec3dをNBTタグから取得する。
     * @param tag NBTタグ。
     * @param key キー。
     * @return 座標。
     */
    public static Vec3d readVec3d(NBTTagCompound tag, String key) {
        NBTTagCompound child = tag.getCompoundTag(key);
        double x = child.getDouble("x");
        double y = child.getDouble("y");
        double z = child.getDouble("z");
        return new Vec3d(x, y, z);
    }

    /**
     * BlockPosをNBTタグに書きこむ。
     * @param tag NBTタグ。
     * @param key キー。
     * @param value 座標。
     */
    public static void writeBlockPos(NBTTagCompound tag, String key, BlockPos value) {
        NBTTagCompound child = new NBTTagCompound();
        child.setDouble("x", value.getX());
        child.setDouble("y", value.getY());
        child.setDouble("z", value.getZ());
        tag.setTag(key, child);
    }

    /**
     * BlockPosをNBTタグから取得する。
     * @param tag NBTタグ。
     * @param key キー。
     * @return 座標。
     */
    public static BlockPos readBlockPos(NBTTagCompound tag, String key) {
        NBTTagCompound child = tag.getCompoundTag(key);
        double x = child.getInteger("x");
        double y = child.getInteger("y");
        double z = child.getInteger("z");
        return new BlockPos(x, y, z);
    }

    /**
     * Vec3dをNBTタグに書きこむ。
     * @param tag NBTタグ。
     * @param key キー。
     * @param value 座標。
     */
    public static void writeVec3d(NBTTagCompound tag, String key, Vec3d value) {
        NBTTagCompound child = new NBTTagCompound();
        child.setDouble("x", value.xCoord);
        child.setDouble("y", value.yCoord);
        child.setDouble("z", value.zCoord);
        tag.setTag(key, child);
    }

    /**
     * オブジェクトのリストをNBTタグから取得する。
     * @param tag NBTタグ。
     * @param key キー。
     * @return リスト。
     */
    public static <T extends HasNbtTagData> List<T> readList(NBTTagCompound tag, String key,
            Supplier<T> createter) {
        NBTTagCompound child = tag.getCompoundTag(key);
        if (child == null) {
            return new ArrayList<>();
        }
        int size = child.getInteger("size");
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            NBTTagCompound element = child.getCompoundTag(Integer.toString(i));
            T value = createter.get();
            value.readNbt(element);
            list.add(value);
        }
        return list;
    }

    /**
     * オブジェクトのリストをNBTタグに書きこむ。
     * @param tag NBTタグ。
     * @param key キー。
     * @param list リスト。
     */
    public static <T extends HasNbtTagData> void writeList(NBTTagCompound tag, String key,
            List<T> list) {
        NBTTagCompound child = new NBTTagCompound();
        child.setInteger("size", list.size());
        for (int i = 0; i < list.size(); i++) {
            NBTTagCompound element = new NBTTagCompound();
            list.get(i).writeNbt(element);
            child.setTag(Integer.toString(i), element);
        }
        tag.setTag(key, child);
    }

    /**
     * オブジェクトのリストをNBTタグから取得する。
     * @param tag NBTタグ。
     * @param key キー。
     * @return リスト。
     */
    public static <T> List<T> readListFunction(NBTTagCompound tag, String key,
            BiFunction<NBTTagCompound, String, T> reader) {
        NBTTagCompound child = tag.getCompoundTag(key);
        if (child == null) {
            return new ArrayList<>();
        }
        int size = child.getInteger("size");
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            T value = reader.apply(child, Integer.toString(i));
            list.add(value);
        }
        return list;
    }

    /**
     * オブジェクトのリストをNBTタグに書きこむ。
     * @param tag NBTタグ。
     * @param key キー。
     * @param list リスト。
     */
    public static <T> void writeListFunction(NBTTagCompound tag, String key,
            List<T> list, TripleFunction<NBTTagCompound, String, T> writer) {
        NBTTagCompound child = new NBTTagCompound();
        child.setInteger("size", list.size());
        for (int i = 0; i < list.size(); i++) {
            writer.run(child, Integer.toString(i), list.get(i));
        }
        tag.setTag(key, child);
    }

    /**
     * アイテムをNBTタグから読み込む。
     * @param tag NBTタグ。
     * @param key キー。
     * @return アイテム。
     */
    public static ItemStack readItem(NBTTagCompound tag, String key) {
        NBTTagCompound child = tag.getCompoundTag(key);
        if (child == null) {
            return null;
        }
        return ItemStack.loadItemStackFromNBT(child);
    }

    /**
     * アイテムをNBTタグに書きこむ。
     * @param tag NBTタグ。
     * @param key キー。
     * @param stack アイテム。
     */
    public static void writeItem(NBTTagCompound tag, String key, ItemStack stack) {
        NBTTagCompound child = new NBTTagCompound();
        if (stack == null) {
            return;
        }
        stack.writeToNBT(child);
        tag.setTag(key, child);
    }

    /**
     * NBTタグから数字の取得。
     * @param key キー。
     * @param stack アイテム。
     * @return 数字。
     */
    public static Optional<Integer> getInterger(String key, ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) {
            return Optional.empty();
        }
        if (!compound.hasKey(key)) {
            return Optional.empty();
        }
        return Optional.ofNullable(compound.getInteger(key));
    }

    /**
     * NBTタグから数字の取得。
     * @param key キー。
     * @param stack アイテム。
     * @param value 数字。
     */
    public static void setInterger(String key, ItemStack stack, int value) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) {
            compound = new NBTTagCompound();
            stack.setTagCompound(compound);
        }
        compound.setInteger(key, value);
    }
}
