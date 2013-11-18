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
package jp.classmethod.testing.internal;

import java.lang.reflect.Field;

public class AssertionErrorMessages {

    public static String toString(Object actual, Object expected) {
        return String.format("Expected: %s%nActual: %s", expected, actual);
    }

    public static AssertionError insert(AssertionError e, String message) {
        String newMessage = String.format("%s%n--%n%s", message, e.getMessage());
        try {
            Field field = Throwable.class.getDeclaredField("detailMessage");
            field.setAccessible(true);
            field.set(e, newMessage);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return e;
    }
}
