package com.github.highd120.util.block;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Blockであることを示すアノテーション。
 * @author hdgam
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BlockRegister {
    /**
     * ブロックの登録名の所得。
     * @return 登録名。
     */
    String name();
}
