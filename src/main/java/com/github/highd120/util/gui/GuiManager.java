package com.github.highd120.util.gui;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.highd120.BotaniaExMain;
import com.google.common.reflect.ClassPath;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * GUIのマネージャー。
 * @author hdgam
 */
public class GuiManager implements IGuiHandler {
    public static List<Class<?>> classList = new ArrayList<>();
    public static List<Class<?>> serverClassList = new ArrayList<>();

    static {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            classList = ClassPath.from(loader)
                    .getTopLevelClasses("com.github.highd120.gui").stream()
                    .map(info -> info.load())
                    .filter(cl -> isHaveAnnotation(cl.getDeclaredAnnotations(), Gui.class))
                    .collect(Collectors.toList());
            serverClassList = classList.stream()
                    .map(clazz -> getServer(clazz))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getServer(Class<?> clazz) {
        return Optional.ofNullable(clazz.getAnnotation(Gui.class))
                .map(annotation -> annotation.server())
                .orElse(null);
    }

    /**
     * Diの処理を行う。
     * @param instance インスタンス。
     * @param field フィールド。
     * @param player プレイヤー。
     * @param world ワールド。
     * @param x x座標。
     * @param y y座標。
     * @param z z座標。
     */
    private static void setDiGui(Object instance, Field field, EntityPlayer player, World world,
            int x, int y, int z) {
        field.setAccessible(true);
        try {
            Class<?> clazz = field.getType();
            if (clazz.equals(EntityPlayer.class)) {
                field.set(instance, player);
            }
            if (clazz.equals(World.class)) {
                field.set(instance, world);
            }
            if (clazz.equals(BlockPos.class)) {
                field.set(instance, new BlockPos(x, y, z));
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * GUIを開く。
     * @param player プレイヤー。
     * @param world ワールド。
     * @param hand イベントを起こした手。
     * @param clazz GUIのクラス。
     */
    public static void playerOpednGui(EntityPlayer player, World world, EnumHand hand,
            Class<?> clazz) {
        if (hand != EnumHand.MAIN_HAND) {
            return;
        }
        player.openGui(BotaniaExMain.instance, classList.indexOf(clazz), world, (int) player.posX,
                (int) player.posY, (int) player.posZ);
    }

    private static boolean isHaveAnnotation(Annotation[] annotations, Class<?> clazz) {
        return Arrays.stream(annotations).anyMatch(an -> an.annotationType().equals(clazz));
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y,
            int z) {
        try {
            Class<?> clazz = serverClassList.get(id);
            if (clazz == null) {
                return null;
            }
            Object instance = clazz.getDeclaredConstructor().newInstance();
            Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> isHaveAnnotation(field.getDeclaredAnnotations(),
                            GuiField.class))
                    .forEach(filed -> GuiManager.setDiGui(instance, filed, player, world, x, y, z));
            clazz.getMethod("init").invoke(instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y,
            int z) {
        try {
            Class<?> clazz = classList.get(id);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> isHaveAnnotation(field.getDeclaredAnnotations(),
                            GuiField.class))
                    .forEach(filed -> GuiManager.setDiGui(instance, filed, player, world, x, y, z));
            clazz.getMethod("init").invoke(instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
}
