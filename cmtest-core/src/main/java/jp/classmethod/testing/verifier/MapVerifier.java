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

import java.util.Map;

import jp.classmethod.testing.internal.AssertionErrorMessages;

public class MapVerifier<T> {

    private ObjectVerifier<T> verifier;

    /**
     * インスタンスを生成する。
     * @param verifier 各要素を比較する{@link ObjectVerifier}
     */
    public MapVerifier(ObjectVerifier<T> verifier) {
        if (verifier == null) throw new IllegalArgumentException("verifier cant't be null.");
        this.verifier = verifier;
    }

    /**
     * 反復要素の比較検証を行う。
     * @param actual Mapの実測値
     * @param expected Mapの期待値
     */
    public void verify(Map<?, T> actual, Map<?, T> expected) {
        if (expected == null) {
            assertThat(actual, is(nullValue()));
            return;
        } else if (actual == null) {
            throw new AssertionError("actual is null, but expected is :" + expected);
        }
        assert actual != null && expected != null;
        int actualSize = actual.size();
        int expectedSize = expected.size();
        if (actualSize != expectedSize) {
            String msg = String.format("Size is unmatched.%nExpected size: %s%nActual size: %s%n%s", expectedSize,
                    actualSize, AssertionErrorMessages.toString(actual, expected));
            throw new AssertionError(msg);
        }
        for (Object key : expected.keySet()) {
            if (!actual.containsKey(key)) {
                String msg = String.format(
                        "Unmatch keyset.%nActual map NOT contains key: %s%nExpected keys:%s%nActual keys:%s%n%s", key,
                        actual.keySet(), expected.keySet(), AssertionErrorMessages.toString(actual, expected));
                throw new AssertionError(msg);
            }
            try {
                verifier.verifyObject(actual.get(key), expected.get(key));
            } catch (AssertionError e) {
                String msg = String.format("AssertionError of key: %s%n%s", key,
                        AssertionErrorMessages.toString(actual, expected));
                throw AssertionErrorMessages.insert(e, msg);
            }
        }
    }
}
