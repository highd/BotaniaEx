package com.github.highd120.util;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * コレクションのユーティリティ。
 * @author hdgam
 */
public class CollectionUtil {
    /**
     * コレクションから検索を行う。
     * @param iterable コレクション。
     * @param predicate 判定用の関数。
     * @return 検索結果。
     */
    public static <T> Optional<T> findIf(Iterable<T> iterable, Predicate<T> predicate) {
        Iterator<T> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            T element = iterator.next();
            if (predicate.test(element)) {
                return Optional.of(element);
            }
        }
        return Optional.empty();
    }
}
