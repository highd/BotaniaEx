package com.github.highd120.util.gui;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.highd120.BotaniaExMain;
import com.google.common.reflect.ClassPath;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiManager implements IGuiHandler {
	public static List<Class<?>> classList = new ArrayList<>();
	static {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			classList = ClassPath.from(loader)
			        .getTopLevelClasses("com.github.highd120.gui").stream()
			        .map(info -> info.load())
			        .filter(cl->isHaveAnnotation(cl.getDeclaredAnnotations(), Gui.class))
			        .collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void DIGui(Object instance, Field field, EntityPlayer player, World world, int x, int y, int z) {
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
	public static void playerOpenGui(EntityPlayer player, World world, Class<?> clazz) {
		player.openGui(BotaniaExMain.instance, classList.indexOf(clazz), world, (int)player.posX, (int)player.posY, (int)player.posZ);
	}
	private static boolean isHaveAnnotation(Annotation[] annotations,Class clazz) {
		return Arrays.stream(annotations).anyMatch(an->an.annotationType().equals(clazz));
	}
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		try {
			Class<?> clazz = classList.get(ID);
			Object instance = clazz.getDeclaredConstructor().newInstance();
			Arrays.stream(clazz.getDeclaredFields())
				.filter(field->isHaveAnnotation(field.getDeclaredAnnotations(), GuiField.class))
				.forEach(filed->GuiManager.DIGui(instance, filed, player, world, x, y, z));
			return instance;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}
