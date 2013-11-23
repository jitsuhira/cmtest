package jp.classmethod.testing.examples.database;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.classmethod.testing.verifier.IterableVerifier;
import jp.classmethod.testing.verifier.ObjectVerifier;

public class UserVerifier extends ObjectVerifier<User> {

    public static void verify(User actual, User expected) throws Exception {
        new UserVerifier().verifyObject(actual, expected);
    }

    public static void verify(Iterable<User> actual, Iterable<User> expected) throws Exception {
        new IterableVerifier<User>(new UserVerifier()).verify(actual, expected);
    }

    @Override
    public void verifyNotNullObject(User actual, User expected) throws AssertionError {
        assertThat("id", actual.id, is(expected.id));
        assertThat("name", actual.name, is(expected.name));
        assertThat("age", actual.age, is(expected.age));
    }

}
