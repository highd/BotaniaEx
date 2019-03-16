package com.github.highd120.util;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;


public class CollectionUtil {
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
