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
package jp.classmethod.testing.internal;

public class PreConditions {

    public static <T> T checkNotNull(T object, String name) {
        if (object == null) {
            throw new NullPointerException(name + " is null.");
        }
        return object;
    }

    public static <T extends CharSequence> T checkNotEmpty(T chars, String name) {
        if (chars == null) {
            throw new NullPointerException(name + " is null.");
        }
        if (chars.length() == 0) {
            throw new IllegalArgumentException(name + " is empty.");
        }
        return chars;
    }
}
