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
import jp.classmethod.testing.internal.AssertionErrorMessages;

/**
 * オブジェクトの比較検証を行うクラス。
 * 
 * プロジェクト固有のクラス毎に本クラスのサブクラスを作成し、 比較検証処理は
 * {@link ObjectVerifier#verifyNotNullObject(Object, Object)} に実装する。
 * 
 * @param <T> 比較する型
 * @author shuji
 * @since 1.0
 */
public abstract class ObjectVerifier<T> {

    /**
     * 比較検証を行う時に呼び出すメソッド。
     * 
     * nullチェックを行った後に、verifyNotNullObjectメソッドを呼び出す。
     * 
     * @param actual テストの実測値
     * @param expected テストの期待値
     * @throws AssertionError テストの期待値がnullであり、テストの実測値がnullでない場合
     * @throws AssertionError テストの期待値がnullでなく、テストの実測値がnullである場合
     * @throws AssertionError verifyNotNullObjectの検証結果が、正しくない場合
     */
    public void verifyObject(T actual, T expected) {
        if (expected == null) {
            assertThat(actual, is(nullValue()));
        } else if (actual == null) {
            throw new AssertionError("actual is null, but expected is :" + expected);
        } else {
            try {
                verifyNotNullObject(actual, expected);
            } catch (AssertionError e) {
                throw AssertionErrorMessages.insert(e, AssertionErrorMessages.toString(actual, expected));
            }
        }
    }

    /**
     * 実測値と期待値を比較検証する。
     * 
     * 比較検証はJUnitのアサーションフレームワークを使うことを想定している。 実装クラスでは、検証結果が正しくない場合、
     * {@link AssertionError}を送出すること。
     * 
     * @param actual テストの実測値 (NOT null)
     * @param expected テストの期待値 (NOT null)
     * @throws AssertionError 検証結果が正しくない場合
     */
    public abstract void verifyNotNullObject(T actual, T expected) throws AssertionError;

}
