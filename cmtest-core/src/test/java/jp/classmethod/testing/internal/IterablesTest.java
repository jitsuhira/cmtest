package jp.classmethod.testing.internal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class IterablesTest {

    @Test
    public void sizeは空リストで0を返す() {
        List<String> list = Collections.emptyList();
        assertThat(Iterables.size(list), is(0));
    }

    @Test
    public void sizeは1要素のリストで1を返す() {
        List<String> list = Arrays.asList("Hello");
        assertThat(Iterables.size(list), is(1));
    }

    @Test
    public void sizeは2要素のリストで2を返す() {
        List<String> list = Arrays.asList("Hello", "World");
        assertThat(Iterables.size(list), is(2));
    }

    @Test
    public void sizeは2要素のSetで2を返す() {
        Set<String> set = new HashSet<>(Arrays.asList("Hello", "World"));
        assertThat(Iterables.size(set), is(2));
    }

    @Test
    public void sizeはCollectionでないIterableで2を返す() {
        final List<String> list = Arrays.asList("Hello", "World");
        Iterable<String> iterable = new Iterable<String>() {

            @Override
            public Iterator<String> iterator() {
                return list.iterator();
            }
        };
        assertThat(Iterables.size(iterable), is(2));
    }

}
