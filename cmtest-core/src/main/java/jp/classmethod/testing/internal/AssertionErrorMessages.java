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
