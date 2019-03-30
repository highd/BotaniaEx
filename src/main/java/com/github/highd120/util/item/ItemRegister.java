package com.github.highd120.util.item;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Itemであることを示すアノテーション。
 * @author hdgam
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ItemRegister {
    /**
     * アイテムの登録名の所得。
     * @return 登録名。
     */
    String name();
}
