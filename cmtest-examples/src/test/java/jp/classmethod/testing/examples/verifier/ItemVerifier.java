/*
 * Copyright 2013 Classmethod, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.classmethod.testing.examples.verifier;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jp.classmethod.testing.verifier.IterableVerifier;
import jp.classmethod.testing.verifier.ObjectVerifier;

import org.junit.Test;

public class ItemVerifier extends ObjectVerifier<Item> {

	/**
	 * Itemオブジェクトの比較検証を行う。
	 * @param actual
	 * @param expected
	 */
	public static void verify(Item actual, Item expected) {
		new ItemVerifier().verifyObject(actual, expected);
	}

	/**
	 * Itemの反復オブジェクトの比較検証を行う。
	 * @param actual
	 * @param expected
	 */
	public static void verify(Iterable<Item> actual, Iterable<Item> expected) {
		new IterableVerifier<Item>(new ItemVerifier()).verify(actual, expected);
	}

	@Override
	public void verifyNotNullObject(Item actual, Item expected) throws AssertionError {
		assertThat("id", actual.id, is(expected.id));
		assertThat("name", actual.name, is(expected.name));
		assertThat("price", actual.price, is(expected.price));
		assertThat("description", actual.description, is(expected.description));
	}

	@Test
	public void createdを除外した検証() throws Exception {
		// Setup
        Item expected = newItem(1L, "test", 2000, "説明", null);
        Item actual = newItem(1L, "test", 2000, "説明", new Date());
		// Verify
		verify(actual, expected);
	}

	@Test
	public void Iterableオブジェクトを比較する() throws Exception {
		// Setup
		List<Item> expected = Arrays.asList(
				newItem(1L, "test1", 2000, "説明1", new Date()),
				newItem(2L, "test2", 2001, "説明2", new Date()));
		List<Item> actual = Arrays.asList(
				newItem(1L, "test1", 2000, "説明1", null),
				newItem(2L, "test2", 2001, "説明2", null));
		// Verify
		verify(actual, expected);
	}

    static Item newItem(Long id, String name, Integer price, String desc, Date createdAt) {
        Item obj = new Item();
        obj.id = id;
        obj.name = name;
        obj.price = price;
        obj.description = desc;
        obj.createdAt = createdAt;
        return obj;
    }

}
