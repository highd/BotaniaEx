package com.github.highd120.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.reflect.ClassPath;

public class ClassUtil {
    /**
     * 指定パス以下の指定アノテーションがついているクラスの一覧を取得。
     * @param path 指定パス。
     * @param annotation 指定アノテーション。
     * @return クラスの一覧。
     */
    public static <T extends Annotation> List<Class<?>> getClassList(String path,
            Class<T> annotation) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            return ClassPath.from(loader)
                    .getTopLevelClassesRecursive(path).stream()
                    .map(info -> info.load())
                    .filter(cl -> cl.isAnnotationPresent(annotation))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * インスタンスの作成。
     * @param clazz クラス。
     * @return インスタンス。
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
}
