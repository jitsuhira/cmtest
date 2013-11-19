/**
 * JUnitの比較検証をサポートする仕組みを提供する。
 * 
 * <code><pre>
 * public class Item {
 *    Long id;
 *    String name;
 *    Integer price;
 *    String description;
 *    Date createdAt;
 *    &#064;Override public String toString() { /&#8727; 省略 &#8727;/ }
 * }
 * </pre></code>
 * 
 * <h3>オブジェクトのカスタムアサーション</h3>
 * <p>{@link jp.classmethod.testing.verifier.ObjectVerifier}のサブクラスを作成する子とっで、オブジェクトのカスタムアサーションを定義する。</p>
 * <p>createdAt以外のフィールドをアサーション対象とするサンプル。</p>
 * <code><pre>
 * public class ItemVerifier extends ObjectVerifier&lt;Item&gt; {
 * 
 *     /&#8727;&#8727;
 *      &#8727; Itemオブジェクトの比較検証を行う。
 *      &#8727; @param actual
 *      &#8727; @param expected
 *      &#8727;/
 *     public static void verify(Item actual, Item expected) {
 *        new ItemVerifier().verifyObject(actual, expected);
 *     }
 *     
 *     &#064;Override
 *     public void verifyNotNullObject(Item actual, Item expected) throws AssertionError {
 *         assertThat("id", actual.id, is(expected.id));
 *         assertThat("name", actual.name, is(expected.name));
 *         assertThat("price", actual.price, is(expected.price));
 *         assertThat("description", actual.description, is(expected.description));
 *     }
 * }
 * </pre></code>
 * <p>テストコードでは次のように利用する。</p>
 * <code><pre>
 * // ItemVerifier.* をstaticインポートする
 * Item expected = ...
 * Item actual = sut.doSomething();
 * verify(actual, expected);
 * </pre></code>
 * @author shuji
 */
package jp.classmethod.testing.verifier;

