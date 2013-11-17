package jp.classmethod.testing.verifier;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import jp.classmethod.testing.internal.AssertionErrorMessages;
import jp.classmethod.testing.internal.Iterables;

/**
 * 反復要素（{@link Iterable}）に対する比較検証クラス。
 * 
 * 実測値と期待値のサイズを比較し、サイズが異なる場合はAssertionErrorを送出する。
 * サイズが同じ場合は各要素に対し、 {@link ObjectVerifier}を適用して比較検証を行う。
 * {@link ObjectVerifier}を指定しない場合は、単純なequalsによる比較で比較検証する。
 * 
 * @author shuji
 * @param <T> 比較検証する反復要素の型
 */
public class IterableVerifier<T> {

    private ObjectVerifier<T> verifier;

    /**
     * インスタンスを生成する。
     * @param verifier
     */
    public IterableVerifier(ObjectVerifier<T> verifier) {
        if (verifier == null)
            throw new IllegalArgumentException("verifier cant't be null.");
        this.verifier = verifier;
    }

    /**
     * 反復要素の比較検証を行う。
     * @param actual
     * @param expected
     */
    public void verify(Iterable<T> actual, Iterable<T> expected) {
        if (expected == null) {
            assertThat(actual, is(nullValue()));
            return;
        } else if (actual == null) {
            throw new AssertionError("actual is null, but expected is :" + expected);
        }
        int actualSize = Iterables.size(actual);
        int expectedSize = Iterables.size(expected);
        if (actualSize != expectedSize) {
            String msg = String.format("Size is unmatched.%nExpected size: %s%nActual size: %s%n%s", expectedSize,
                    actualSize, AssertionErrorMessages.toString(actual, expected));
            throw new AssertionError(msg);
        }
        int index = 0;
        Iterator<T> actualIter = actual.iterator();
        Iterator<T> expectedIter = expected.iterator();
        while (actualIter.hasNext()) {
            try {
                verifier.verifyObject(actualIter.next(), expectedIter.next());
            } catch (AssertionError e) {
                String msg = String.format("AssertionError at index: %s%n%s", index,
                        AssertionErrorMessages.toString(actual, expected));
                throw AssertionErrorMessages.insert(e, msg);
            }
            index++;
        }
    }

}
