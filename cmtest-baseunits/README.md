# cmtest-baseunits
このモジュールは、[BaseUnits Library](https://github.com/dai0304/baseunits/)に依存したJUnitのテストコードを書く時に便利なヘルパーライブラリです。

- [FixClock](#FixClock)

## 依存ライブラリ
- [JUnit](http://junit.org/) 4.11+
- [BaseUnits Library](https://github.com/dai0304/baseunits/) 2.11+

## Examples

### FixClock
jp.xet.baseunits.timeutil.Clockクラスの時刻を固定化するルールです。

#### サンプル
```java
public class FixClockExample {
    @Rule
    public FixClock fixClock = FixClock.at(TimePoint.atUTC(2013, 11, 10, 12, 23, 34));

    @Test
    public void Clockのnowメソッドは固定化された時刻を返す() throws Exception {
        // Exercise
        TimePoint now = Clock.now();
        // Verify
        assertThat(now, is(TimePoint.atUTC(2013, 11, 10, 12, 23, 34)));
    }
}
```
