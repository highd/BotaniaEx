package com.github.highd120.util.subtile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SubTileEntityであることを示すアノテーション。
 * @author hdgam
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SubTileRegister {
    /**
     * SubTileEntityの登録名の所得。
     * @return 登録名。
     */
    String name();
}
