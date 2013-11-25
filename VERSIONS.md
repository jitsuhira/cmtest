# バージョン情報
## 0.3 - 2013/11/25
- [cmtest-baseunits](./cmtest-baseunits/)を追加
	- FixClock - Clockによる現在時刻を固定化するルール

## 0.2 - 2013/11/23
- [cmtest-db](./cmtest-db/) を追加
	- YamlDataSet - YAMLによるDbUnitのデータセット
	- DbUnitTester - DbUnitをJUnit4で使うためのRule
	- Fixture - DbUnitTesterで宣言的にフィクスチャを指定するアノテーション
- [cmtest-core](./cmtest-core/)
	- ObjectVerifierでチェック例外を伝搬できるようにした

## 0.1 - 2013/11/20
- [cmtest-core](./cmtest-core/) を追加
	- ObjectVerifier - オブジェクトのカスタムアサーション
	- IterableVerifier - ObjectVerifierをリストで使う
	- MapVerifier - ObjectVerifierをMapで使う
	- ArrayVerifier - ObjectVerifierを配列で使う
	- DefaultTimeZone - TimeZoneを固定化するRule
	- FixtureUtils - フィクスチャをセットアップするためのユーティリティクラス
- 補足記事[「JUnitのカスタムアサーションを簡単に実装できるcmtest - Developers.IO」](http://dev.classmethod.jp/testing/unittesting/custom-assertion-using-junit/)