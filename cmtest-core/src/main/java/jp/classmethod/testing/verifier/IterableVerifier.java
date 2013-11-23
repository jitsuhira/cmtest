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
package jp.classmethod.testing.verifier;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import jp.classmethod.testing.internal.AssertionErrorMessages;
import jp.classmethod.testing.internal.Iterables;

/**
 * 反復要素（{@link Iterable}）に対する比較検証クラス。
 * 
 * 実測値と期待値のサイズを比較し、サイズが異なる場合はAssertionErrorを送出する。
 * サイズが同じ場合は各要素に対し、 {@link ObjectVerifier}を適用して比較検証を行う。
 * {@link ObjectVerifier}を指定しない場合は、単純なequalsによる比較で比較検証する。
 * 
 * @author shuji
 * @param <T> 比較検証する反復要素の型
 */
public class IterableVerifier<T> {

    private ObjectVerifier<T> verifier;

    /**
     * equalsメソッドで比較検証するObjectVerifierで、インスタンスを生成する。
     */
    public IterableVerifier() {
        this(new ObjectVerifier.EqualsObjectVerifier<T>());
    }

    /**
     * ObjectVerifierを指定し、インスタンスを生成する。
     * @param verifier カスタム検証ルールを実装した{@link ObjectVerifier}
     */
    public IterableVerifier(ObjectVerifier<T> verifier) {
        if (verifier == null) throw new IllegalArgumentException("verifier cant't be null.");
        this.verifier = verifier;
    }

    /**
     * 反復要素の比較検証を行う。
     * @param actual 反復要素の実測値
     * @param expected 反復要素の期待値
     */
    public void verify(Iterable<T> actual, Iterable<T> expected) throws Exception {
        if (expected == null) {
            assertThat(actual, is(nullValue()));
            return;
        } else if (actual == null) {
            throw new AssertionError("actual is null, but expected is :" + expected);
        }
        int actualSize = Iterables.size(actual);
        int expectedSize = Iterables.size(expected);
        if (actualSize != expectedSize) {
            String msg = String.format("Size is unmatched.%nExpected size: %s%nActual size: %s%n%s", expectedSize,
                    actualSize, AssertionErrorMessages.toString(actual, expected));
            throw new AssertionError(msg);
        }
        int index = 0;
        Iterator<T> actualIter = actual.iterator();
        Iterator<T> expectedIter = expected.iterator();
        while (actualIter.hasNext()) {
            try {
                verifier.verifyObject(actualIter.next(), expectedIter.next());
            } catch (AssertionError e) {
                String msg = String.format("AssertionError at index: %s%n%s", index,
                        AssertionErrorMessages.toString(actual, expected));
                throw AssertionErrorMessages.insert(e, msg);
            }
            index++;
        }
    }

}
