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
package jp.classmethod.testing.fixtures;

import java.lang.reflect.Field;

/**
 * フィクスチャを構築する場合に使うユーティリティクラス。
 * 
 * @since 1.0
 * @author shuji
 */
public class FixtureUtils {

    /**
     * プライベートフィールドなどアクセスできないフィールドに値を設定するためのヘルパクラスを作成する。
     * 
     * @param target 値を設定するオブジェクト
     * @return FieldInjecterクラス
     * @since 1.0
     */
    public static <T> FieldInjecter<T> injectTo(T target) {
        if (target == null) throw new NullPointerException("target can't be null.");
        return new FieldInjecter<T>(target);
    }

    /**
     * プライベートフィールドなどアクセスできないフィールドに値を設定するためのヘルパクラス
     * 
     * @since 1.0
     * @author shuji
     */
    public static class FieldInjecter<T> {
        private T target;
        private Class<?> clazz;

        FieldInjecter(T target) {
            this.target = target;
            clazz = target.getClass();
        }

        /**
         * フィールド名を指定し、値を設定する。
         * <p>本メソッドでは、セッターメソッドなどは無視し、フィールド名に対してリフレクションAPIで設定する。</p>
         * 
         * @param fieldName フィールド名
         * @param value 設定する値
         * @return このオブジェクト
         * @throws AssertionError なんらかの例外が発生した場合にラップされる
         */
        public FieldInjecter<T> field(String fieldName, Object value) {
            try {
                Field field = findField(clazz, fieldName);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(target, value);
                return this;
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }

        private Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                Class<?> superClass = clazz.getSuperclass();
                if (superClass == null) {
                    throw e;
                }
                return findField(superClass, fieldName);
            }
        }
        
        /**
         * オブジェクトを返す。
         * @return フィールドを設定されたオブジェクト
         */
        public T returnObject() {
            return this.target;
        }
    }
}
