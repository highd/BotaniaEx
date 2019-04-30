package com.github.highd120.util.gui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * GUIであることを示すアノテーション。
 * @author hdgam
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Gui {
    /**
     * サーバーのGUIの取得。
     * @return サーバーのGUI。
     */
    Class<?> server();
}
