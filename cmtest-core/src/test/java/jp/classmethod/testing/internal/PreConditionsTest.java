package jp.classmethod.testing.internal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PreConditionsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void checkNotNullはnullで例外をthrowする() throws Exception {
        // Setup
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("arg is null.");
        // Exercise
        PreConditions.checkNotNull(null, "arg");
    }

    @Test
    public void checkNotNullは非nullで引数で渡したオブジェクトを返す() throws Exception {
        // Setup
        Object obj = new Object();
        // Exercise
        Object actual = PreConditions.checkNotNull(obj, "arg");
        // Verify
        assertThat(actual, is(sameInstance(obj)));
    }

    @Test
    public void checkNotEmptyはnullで例外をthrowする() throws Exception {
        // Setup
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("str is null.");
        // Exercise
        PreConditions.checkNotEmpty(null, "str");
    }

    @Test
    public void checkNotEmptyは空文字列で例外をthrowする() throws Exception {
        // Setup
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("str is empty.");
        // Exercise
        PreConditions.checkNotEmpty("", "str");
    }

    @Test
    public void checkNotEmptyは非空文字列はそのオブジェクトを返す() throws Exception {
        // Setup
        String str = "hello world";
        // Exercise
        String actual = PreConditions.checkNotNull(str, "str");
        // Verify
        assertThat(actual, is(sameInstance(str)));
    }
}
