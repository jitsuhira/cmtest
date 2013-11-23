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

import java.util.Arrays;

import jp.classmethod.testing.internal.AssertionErrorMessages;

/**
 * 配列に対する比較検証クラス。
 * 
 * 実測値と期待値のサイズを比較し、サイズが異なる場合はAssertionErrorを送出する。
 * サイズが同じ場合は各要素に対し、 {@link ObjectVerifier}を適用して比較検証を行う。
 * {@link ObjectVerifier}を指定しない場合は、単純なequalsによる比較で比較検証する。
 * 
 * @author shuji
 * @param <T> 比較検証する反復要素の型
 */
public class ArrayVerifier<T> {

    private ObjectVerifier<T> verifier;

    /**
     * equalsメソッドで比較検証するObjectVerifierで、インスタンスを生成する。
     */
    public ArrayVerifier() {
        this(new ObjectVerifier.EqualsObjectVerifier<T>());
    }

    /**
     * インスタンスを生成する。
     * @param verifier 各要素を比較する{@link ObjectVerifier}
     */
    public ArrayVerifier(ObjectVerifier<T> verifier) {
        if (verifier == null) throw new IllegalArgumentException("verifier cant't be null.");
        this.verifier = verifier;
    }

    /**
     * 配列の比較検証を行う。
     * @param actual 配列の実測値
     * @param expected 配列の期待値
     */
    public void verify(T[] actual, T[] expected) throws Exception {
        if (expected == null) {
            assertThat(actual, is(nullValue()));
            return;
        } else if (actual == null) {
            throw new AssertionError("actual is null, but expected is :" + expected);
        }
        assert actual != null && expected != null;
        int actualLength = actual.length;
        int expectedLength = expected.length;
        if (actualLength != expectedLength) {
            String msg = String.format("length is unmatched.%nExpected length: %s%nActual length: %s%n%s",
                    expectedLength, actualLength,
                    AssertionErrorMessages.toString(Arrays.toString(actual), Arrays.toString(expected)));
            throw new AssertionError(msg);
        }
        assert actualLength == expectedLength;
        for (int index = 0; index < actualLength; index++) {
            try {
                verifier.verifyObject(actual[index], expected[index]);
            } catch (AssertionError e) {
                String msg = String.format("AssertionError at index: %s%n%s", index,
                        AssertionErrorMessages.toString(Arrays.toString(actual), Arrays.toString(expected)));
                throw AssertionErrorMessages.insert(e, msg);
            }
        }
    }

}
