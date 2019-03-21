package com.github.highd120.item;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通常のアイテム。
 * @author hdgam
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BasicItem {
    /**
     * アイテムの登録名の所得。
     * @return 登録名。
     */
    String name();
}
