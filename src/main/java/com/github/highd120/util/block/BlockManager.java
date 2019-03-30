package com.github.highd120.util.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.highd120.BotaniaExMain;
import com.github.highd120.util.ClassUtil;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockManager {
    private static List<Class<?>> classList = new ArrayList<>();
    private static Map<Class<?>, Block> blockMap = new HashMap<>();

    static {
        classList = ClassUtil.getClassList("com.github.highd120.block", BlockRegister.class);
    }

    /**
     * 初期化。
     */
    public static void init() {
        classList.forEach(clazz -> {
            Object obj = ClassUtil.newInstance(clazz);
            String name = clazz.getAnnotation(BlockRegister.class).name();
            if (obj instanceof Block) {
                Block block = (Block) obj;
                block.setRegistryName(new ResourceLocation(BotaniaExMain.MOD_ID, name));
                block.setUnlocalizedName(BotaniaExMain.MOD_ID + "." + name);
                GameRegistry.register(block);
                blockMap.put(clazz, block);
            } else {
                throw new ClassCastException(clazz.getName());
            }
        });
    }

    /**
     * アイテムブロックの初期化。
     */
    public static void itemBlocInit(Map<Class<?>, Item> itemMap, boolean isClient) {
        blockMap.forEach((key, block) -> {
            ItemBlock item = new ItemBlock(block);
            item.setUnlocalizedName(block.getUnlocalizedName());
            GameRegistry.register(item, block.getRegistryName());
            itemMap.put(key, item);
            if (isClient) {
                ModelLoader.setCustomModelResourceLocation(item, 0,
                        new ModelResourceLocation(item.getRegistryName(), "inventory"));
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends Block> T getBlock(Class<T> clazz) {
        return (T) blockMap.get(clazz);
    }
}
