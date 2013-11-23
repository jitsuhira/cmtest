# cmtest-core
このモジュールは、JUnitのテストコードを書く時に便利なヘルパーライブラリです。

- [ObjectVerifier](#ObjectVerifier)
	- [Listなどの反復要素で使う](#Listなどの反復要素で使う)
	- [Mapで使う](#Mapで使う)
	- [配列で使う](#配列で使う)
	- [ObjectVerifier](#ObjectVerifier)

## 依存ライブラリ
- [JUnit](http://junit.org/) 4.11+

## Examples

### ObjectVerifier
#### 動機
- 検証するクラスのequalsメソッドがオーバーライドできない
- 検証するクラスで一部のフィールドを使ってアサーションを行いたい

#### サンプル
テストで検証されるクラス
```java
public class Item {
	Long id;
	String name;
	Integer price;
	String description;
	Date createdAt;
	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", price=" + price
				+ ", description=" + description + ", createdAt=" + createdAt
				+ "]";
	}
}
```
作成するカスタムアサーションクラス
```java
public class ItemVerifier extends ObjectVerifier<Item> {

	/**
	 * Itemオブジェクトの比較検証を行う。
	 * @param actual
	 * @param expected
	 */
	public static void verify(Item actual, Item expected) {
		new ItemVerifier().verifyObject(actual, expected);
	}
	
    @Override
	public void verifyNotNullObject(Item actual, Item expected) throws AssertionError {
		assertThat("id", actual.id, is(expected.id));
		assertThat("name", actual.name, is(expected.name));
		assertThat("price", actual.price, is(expected.price));
		assertThat("description", actual.description, is(expected.description));
	}
}
```
テストコード
```java
	@Test
	public void createdを除外した検証() throws Exception {
		// Setup
        Item expected = newItem(1L, "test", 2000, "説明", null);
        Item actual = newItem(1L, "test", 2000, "説明", new Date());
		// Verify
		verify(actual, expected); // => GREEN
	}
```  
#### Listなどの反復要素で使う
IterableVerifierクラスを利用する。
```java
public class ItemVerifier extends ObjectVerifier<Item> {
	/**
	 * Itemの反復オブジェクトの比較検証を行う。
	 * @param actual
	 * @param expected
	 */
	public static void verify(Iterable<Item> actual, Iterable<Item> expected) {
		new IterableVerifier<Item>(new ItemVerifier()).verify(actual, expected);
	}
    // 以下略
}
```
テストコード
```java
	@Test
	public void Iterableオブジェクトを比較する() throws Exception {
		// Setup
		List<Item> expected = Arrays.asList(
				newItem(1L, "test1", 2000, "説明1", new Date()),
				newItem(2L, "test2", 2001, "説明2", new Date()));
		List<Item> actual = Arrays.asList(
				newItem(1L, "test1", 2000, "説明1", null),
				newItem(2L, "test2", 2001, "説明2", null));
		// Verify
		verify(actual, expected);
	}
```
#### Mapで使う
MapVerifierクラスを利用する。
```java
public class ItemVerifier extends ObjectVerifier<Item> {
    /**
     * ItemのMapの比較検証を行う。
     * @param actual
     * @param expected
     */
    public static void verify(Map<?, Item> actual, Map<?, Item> expected) {
        new MapVerifier<Item>(new ItemVerifier()).verify(actual, expected);
    }
    // 以下略
}
```
テストコード
```java
    @Test
    public void Mapを比較する() throws Exception {
        // Setup
        Map<Long, Item> expected = new HashMap<>();
        expected.put(1L, newItem(1L, "test1", 2000, "説明1", new Date()));
        expected.put(2L, newItem(2L, "test2", 2001, "説明2", new Date()));
        Map<Long, Item> actual = new HashMap<>();
        actual.put(1L, newItem(1L, "test1", 2000, "説明1", null));
        actual.put(2L, newItem(2L, "test2", 2001, "説明2", null));
        // Verify
        verify(actual, expected);
    }
```

#### 配列で使う
ArrayVerifierクラスを利用する。
```java
public class ItemVerifier extends ObjectVerifier<Item> {
    /**
     * Itemの配列の比較検証を行う。
     * @param actual
     * @param expected
     */
    public static void verify(Item[] actual, Item[] expected) {
        new ArrayVerifier<Item>(new ItemVerifier()).verify(actual, expected);
    }
    // 以下略
}
```
テストコード
```java
    @Test
    public void 配列を比較する() throws Exception {
        // Setup
        Item[] expected = new Item[] {
                null,
                newItem(1L, "test1", 2000, "説明1", new Date()),
                newItem(2L, "test2", 2001, "説明2", new Date()) };
        Item[] actual = new Item[] {
                null,
                newItem(1L, "test1", 2000, "説明1", null),
                newItem(2L, "test2", 2001, "説明", null) };
        // Verify
        verify(actual, expected);
    }

```
