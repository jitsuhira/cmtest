package jp.classmethod.testing.internal;

import java.util.Collection;
import java.util.Iterator;

public class Iterables {

	public static int size(Iterable<?> iterable) {
		if (iterable instanceof Collection<?>) {
			return ((Collection<?>) iterable).size();
		} else {
			return size(iterable.iterator());
		}
	}

	public static int size(Iterator<?> iter) {
		int size = 0;
		for (; iter.hasNext(); iter.next()) {
			size++;
		}
		return size;
	}
}
