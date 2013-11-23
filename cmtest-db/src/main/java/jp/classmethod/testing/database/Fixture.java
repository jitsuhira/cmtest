/*
 * Copyright 2013 Classmethod, Inc.
 *
 * Licensed under the Apace License, Version 2.0 (the "License");
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
package jp.classmethod.testing.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link DbUnitTester}でDataSetとして扱うフィクスチャを指定するアノテーション。
 * <p>
 * resourcesにはリソースを指定する。
 * リソースはクラスパスから参照する。
 * リソース名が/で始まる場合はルートパッケージからの絶対パスとなる（例: /jp/classmethod/testing/fixtures.yaml）
 * リソース名が/で始まらない場合はテストクラスと同じパッケージから検索する（例: users.yaml）
 * <ul>
 * <li>テストクラスに指定した場合、全テストメソッドで共通のフィクスチャが適用される。</li>
 * <li>テストメソッドに指定した場合、そのテストメソッドのみで指定したフィクスチャが適用される。</li>
 * <li>両方に指定した場合は、テストメソッドのフィクスチャが優先される。</li>
 * <li>両方に指定しなかった場合は、空のフィクスチャが設定される。</li>
 * </p>
 * <p>
 * typeにはリソースの種類を指定する。現在、YAMLフォーマットのみ対応。
 * </p>
 * @since 1.0
 * @author shuji
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Fixture {

    String[] resources();

    Fixture.Type type() default Type.YAML;

    public static enum Type {
        YAML;
    }
}
