package com.github.highd120.item;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import com.github.highd120.BotaniaExMain;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * アイテムのリスト。
 * @author hdgam
 */
public class ItemList {
    public static ShotSwordItem shotSwordItem;

    @BasicItem(name = "rune")
    public static Item runeItem;

    /**
     * アイテムの登録。
     * @param event イベント。
     */
    public static void init(FMLPreInitializationEvent event) {
        Field[] fields = ItemList.class.getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(BasicItem.class)) {
                    String name = field.getAnnotation(BasicItem.class).name();
                    Item item = (Item) field.getType().getDeclaredConstructor().newInstance();
                    field.set(null, item);
                    GameRegistry.register(item, new ResourceLocation(BotaniaExMain.MOD_ID, name));
                    item.setUnlocalizedName(BotaniaExMain.MOD_ID + "." + name);
                    if (event.getSide().isClient()) {
                        ModelLoader.setCustomModelResourceLocation((Item) item, 0,
                                new ModelResourceLocation(item.getRegistryName(), "inventory"));
                    }
                } else {
                    IRegiser item = (IRegiser) field.getType().getDeclaredConstructor()
                            .newInstance();
                    field.set(null, item);
                    String name = item.getName();
                    GameRegistry.register((Item) item,
                            new ResourceLocation(BotaniaExMain.MOD_ID, name));
                    ((Item) item).setUnlocalizedName(BotaniaExMain.MOD_ID + "." + name);
                    if (event.getSide().isClient()) {
                        ModelLoader.setCustomModelResourceLocation((Item) item, 0,
                                new ModelResourceLocation(((Item) item).getRegistryName(),
                                        "inventory"));
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }
}
